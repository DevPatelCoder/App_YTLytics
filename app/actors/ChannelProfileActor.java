package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Status;
import models.api.YouTubeService;
import models.data.ChannelVideoData;
import models.data.Constants;
import java.io.IOException;

/**
 * The {@code ChannelProfileActor} handles fetching recent videos and metadata for a specific YouTube channel.
 * It interacts with the {@code YouTubeService} to retrieve channel data and sends the results back to the requesting actor.
 *
 * <p>This actor receives requests encapsulated in {@code SearchChannelVideosRequest} messages
 * and responds with {@code SearchChannelVideosResponse} messages containing the retrieved channel video data.
 */
public class ChannelProfileActor extends AbstractActor {
    private final YouTubeService youTubeService;

    private final ActorRef parentActor;

    /**
     * Constructs a {@code ChannelProfileActor}.
     *
     * @param youTubeService The {@code YouTubeService} instance used to fetch channel data from the YouTube API.
     * @param parentActor    The parent actor that instantiated this actor.
     */
    public ChannelProfileActor(YouTubeService youTubeService, ActorRef parentActor) {
        this.youTubeService = youTubeService;
        this.parentActor = parentActor;

    }

    /**
     * Factory method to create {@code Props} for this actor.
     *
     * @param youTubeService The {@code YouTubeService} instance used to fetch data.
     * @param parentActor    The parent actor reference.
     * @return A {@code Props} instance for creating this actor.
     */
    public static Props props(YouTubeService youTubeService, ActorRef parentActor) {
        return Props.create(ChannelProfileActor.class,
                () -> new ChannelProfileActor(youTubeService, parentActor));
    }

    /**
     * Defines the message handling behavior of this actor.
     *
     * <p>Handles:
     * <ul>
     *     <li>{@code SearchChannelVideosRequest}: Fetches recent videos of the specified channel.</li>
     * </ul>
     *
     * @return The {@code Receive} instance defining the actor's behavior.
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SearchChannelVideosRequest.class, this::handleSearchVideos)
                .build();
    }

    /**
     * Handles the {@code SearchChannelVideosRequest} message to fetch recent videos for a specific channel.
     *
     * @param request The {@code SearchChannelVideosRequest} containing the channel ID.
     */
    private void handleSearchVideos(SearchChannelVideosRequest request) {
        try {
            ChannelVideoData data = youTubeService.getChannelRecentVideos(request.channelId, Constants.MAX_VIDEOS_DISPLAY_COUNT);

            getSender().tell(new SearchChannelVideosResponse(data), getSelf());
        } catch (IOException e) {
            getSender().tell(new Status.Failure(e), getSelf());
        }
    }

    /**
     * Message to request recent videos of a specific YouTube channel.
     */
    public static class SearchChannelVideosRequest {
        public final String channelId;
        /**
         * Constructs a {@code SearchChannelVideosRequest}.
         *
         * @param channelId The ID of the YouTube channel to fetch videos for.
         */
        public SearchChannelVideosRequest(String channelId) {
            this.channelId = channelId;
        }
    }

    /**
     * Message containing the response for a channel videos search.
     */
    public static class SearchChannelVideosResponse {
        public final ChannelVideoData channelVideoData;
        /**
         * Constructs a {@code SearchChannelVideosResponse}.
         *
         * @param channelVideoData The {@code ChannelVideoData} containing the recent videos and metadata.
         */
        public SearchChannelVideosResponse(ChannelVideoData channelVideoData) {
            this.channelVideoData = channelVideoData;
        }
    }


}
