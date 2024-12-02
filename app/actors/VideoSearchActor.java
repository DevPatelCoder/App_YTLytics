package actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.Status;
import models.api.YouTubeService;
import models.data.Constants;
import models.data.VideoData;

import java.io.IOException;
import java.util.List;

/**
 * The {@code VideoSearchActor} is an Akka actor responsible for handling video search requests.
 * It communicates with the {@code YouTubeService} to fetch video data based on the search term provided in the request.
 */
public class VideoSearchActor extends AbstractActor {
    /**
     * A service instance used to interact with the YouTube API for video searches.
     */
    private final YouTubeService youTubeService;

    /**
     * Constructs a {@code VideoSearchActor} with the specified {@code YouTubeService}.
     *
     * @param youTubeService The service to use for searching YouTube videos.
     */
    public VideoSearchActor(YouTubeService youTubeService) {
        this.youTubeService = youTubeService;
    }

    /**
     * Factory method to create a {@code Props} instance for {@code VideoSearchActor}.
     *
     * @param youTubeService The service to use for searching YouTube videos.
     * @return A {@code Props} instance to create {@code VideoSearchActor}.
     */

    public static Props props(YouTubeService youTubeService) {
        return Props.create(VideoSearchActor.class, () -> new VideoSearchActor(youTubeService));
    }

    /**
     * Defines the message-handling behavior of this actor.
     *
     * <p>This actor handles:
     * <ul>
     *     <li>{@code SearchVideosRequest}: Processes a video search request using the YouTube service.</li>
     * </ul>
     *
     * @return The {@code Receive} instance defining the actor's behavior.
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SearchVideosRequest.class, this::handleSearchVideos)
                .build();
    }

    /**
     * Handles the {@code SearchVideosRequest} message to perform a video search.
     *
     * <p>On successful search, it sends back a {@code SearchVideosResponse} containing a list of videos.
     * If an exception occurs, it sends a {@code Status.Failure} message with the exception details.
     *
     * @param request The {@code SearchVideosRequest} message containing the search term.
     */
    private void handleSearchVideos(SearchVideosRequest request) {
        try {
            List<VideoData> videos = youTubeService.searchVideos(
                    request.searchTerm,
                    Constants.MAX_VIDEOS_DISPLAY_COUNT
            );
            getSender().tell(new SearchVideosResponse(videos), getSelf());
        } catch (IOException e) {
            getSender().tell(new Status.Failure(e), getSelf());
        }
    }

    /**
     * Message class representing a request to search for videos.
     */
    public static class SearchVideosRequest {
        /**
         * The search term to use for the video search.
         */
        public final String searchTerm;
        /**
         * Constructs a {@code SearchVideosRequest} with the specified search term.
         *
         * @param searchTerm The term to search for videos.
         */
        public SearchVideosRequest(String searchTerm) {
            this.searchTerm = searchTerm;
        }
    }

    /**
     * Message class representing a response containing video search results.
     */
    public static class SearchVideosResponse {
        /**
         * A list of video data objects representing the search results.
         */
        public final List<VideoData> videos;
        /**
         * Constructs a {@code SearchVideosResponse} with the specified list of videos.
         *
         * @param videos The list of videos returned by the search.
         */
        public SearchVideosResponse(List<VideoData> videos) {
            this.videos = videos;
        }
    }
}
