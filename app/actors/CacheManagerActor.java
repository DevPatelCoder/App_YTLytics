package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Status;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import models.api.YouTubeService;
import models.data.ChannelVideoData;
import models.data.Constants;
import models.data.VideoSearchData;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The CacheManagerActor class serves as a central actor for managing cached data
 * related to YouTube search results, channel metadata, and video tags. It handles
 * requests for cached data, initiates new data retrieval when necessary, and
 * periodically refreshes the cached data to ensure consistency.
 *
 * <p>This actor utilizes a YouTubeService instance to interact with the YouTube Data API
 * and uses Akka's actor model for concurrency and messaging. Cache management is
 * implemented using thread-safe collections.
 */
public class CacheManagerActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    // Cache stores
    private final ConcurrentHashMap<String, VideoSearchData> searchResults = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ChannelVideoData> channelMetaDataCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<String>> videoTagsCache = new ConcurrentHashMap<>();

    private final YouTubeService youTubeService;
    private ActorRef timerActor;


    /**
     * Constructor initializing the CacheManagerActor with a YouTubeService instance.
     *
     * @param youTubeService The YouTubeService instance for API interactions.
     */
    public CacheManagerActor(YouTubeService youTubeService) {
        this.youTubeService = youTubeService;
    }

    /**
     * Constructor initializing the CacheManagerActor with a configuration object.
     *
     * @param config The configuration object containing API keys and other settings.
     * @throws GeneralSecurityException If a security issue occurs while creating the YouTubeService.
     * @throws IOException              If an I/O error occurs during initialization.
     */
    public CacheManagerActor(com.typesafe.config.Config config) throws GeneralSecurityException, IOException {
        String apiKey = config.getString("youtube.api.key");
        this.youTubeService = createYouTubeService(apiKey);
    }

    //Actor Messages
    // Message for timer tick
    public static class RefreshCache {}
    // Actor messages
    public static class SearchRequest {
        public final String searchTerm;
        public SearchRequest(String searchTerm) {
            this.searchTerm = searchTerm;
        }
    }

    public static class ChannelRequest {
        public final String channelId;
        public ChannelRequest(String channelId) {
            this.channelId = channelId;
        }
    }

    public static class VideoTagRequest {
        public final String videoId;
        public VideoTagRequest(String videoId) {
            this.videoId = videoId;
        }
    }

    /**
     * Factory method to create Props for the CacheManagerActor.
     *
     * @param config The configuration object containing API keys and other settings.
     * @return Props instance for creating CacheManagerActor instances.
     * @throws GeneralSecurityException If a security issue occurs while creating the YouTubeService.
     * @throws IOException              If an I/O error occurs during initialization.
     */
    public static Props props(com.typesafe.config.Config config) throws GeneralSecurityException, IOException {
        String apiKey = config.getString("youtube.api.key");
        YouTubeService youTubeService = createYouTubeService(apiKey);
        return Props.create(CacheManagerActor.class, () -> new CacheManagerActor(youTubeService));
    }

    private static YouTubeService createYouTubeService(String apiKey) throws GeneralSecurityException, IOException {
        return new YouTubeService(apiKey);
    }

    // Actor lifecycle methods
    @Override
    public void preStart() {

        timerActor = getContext().actorOf(
                CacheRefreshTimerActor.props(),
                "cache-refresh-timer"
        );


        timerActor.tell(
                new CacheRefreshTimerActor.StartTimer("searchCache", Constants.REFRESH_RATE_IN_SECONDS),
                getSelf()
        );
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SearchRequest.class, this::handleSearchRequest)
                .match(ChannelRequest.class, this::handleChannelRequest)
                .match(VideoTagRequest.class, this::handleVideoTagRequest)
                .match(VideoSearchData.class, result -> {
                    // Cache the search result when received from coordinator
                    searchResults.put(result.getQuery(), result);
                    // Forward the result to the original sender if it's not ourselves
                    if (!getSender().equals(getSelf())) {
                        getSender().tell(result, getSelf());
                    }
                })
                .match(ChannelProfileActor.SearchChannelVideosResponse.class, result -> {
                    channelMetaDataCache.put(result.channelVideoData.getVideoDataList().get(0).getChannelId(), result.channelVideoData);
                    if (!getSender().equals(getSelf())) {
                        getSender().tell(result.channelVideoData, getSelf());
                    }
                })
                .match(RefreshCache.class, msg -> handleRefreshCache())
                .build();
    }

    @Override
    public void postStop() {
        // Stop the timer when the CacheManager is stopped
        if (timerActor != null) {
            timerActor.tell(
                    new CacheRefreshTimerActor.StopTimer("searchCache"),
                    getSelf()
            );
        }
    }

    private void handleSearchRequest(SearchRequest request) {
        VideoSearchData cachedResult = searchResults.get(request.searchTerm);
        if (cachedResult != null) {
            getSender().tell(cachedResult, getSelf());
            return;
        }


        ActorRef coordinator = getContext().actorOf(
                SearchCoordinatorActor.props(
                        request.searchTerm,
                        youTubeService,
                        getSelf()
                ),
                "search-coordinator-" + request.searchTerm.hashCode()
        );


        coordinator.tell(getSender(), getSelf());
    }

    private void handleChannelRequest(ChannelRequest request) {
        ChannelVideoData cachedResult = channelMetaDataCache.get(request.channelId);
        if (cachedResult != null) {
            getSender().tell(cachedResult, getSelf());
            return;
        }

        ActorRef channelProfileActor = getContext().actorOf(
                ChannelProfileActor.props(youTubeService, getSelf()),
                "channel-profile-search-" + request.channelId.hashCode());

        // Forward the original sender to handle the response
        channelProfileActor.tell(new ChannelProfileActor.SearchChannelVideosRequest(request.channelId), getSelf());

    }

    private void handleVideoTagRequest(VideoTagRequest request) {
        try {
            List<String> cachedTags = videoTagsCache.get(request.videoId);

            if (cachedTags == null) {
                cachedTags = youTubeService.getVideoTags(request.videoId);
                videoTagsCache.put(request.videoId, cachedTags);
            }

            getSender().tell(cachedTags, getSelf());
        } catch (IOException e) {
            getSender().tell(new Status.Failure(e), getSelf());
        }
    }

    private void handleRefreshCache() {
        log.info("Starting cache refresh");
        // Iterate through all cached search terms and refresh them
        searchResults.keySet().forEach(searchTerm -> {
            // Create a coordinator actor for each search term
            ActorRef coordinator = getContext().actorOf(
                    SearchCoordinatorActor.props(
                            searchTerm,
                            youTubeService,
                            getSelf()
                    ),
                    "refresh-coordinator-" + searchTerm.hashCode() + "-" + System.currentTimeMillis()
            );

            // Send ourselves as the sender to receive the updated results
            coordinator.tell(getSelf(), getSelf());
        });
    }
}