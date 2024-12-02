package actors;

import akka.actor.*;
import models.api.YouTubeService;
import models.data.VideoData;
import play.mvc.Result;
import play.mvc.Results;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

/**
 * The {@code TagActor} class is responsible for searching videos on YouTube based on a provided tag.
 *
 * <p>This actor communicates with the {@code YouTubeService} to perform video searches asynchronously.
 * It processes incoming search requests, interacts with the YouTube API to fetch video data, and sends the results
 * back to the requester. The actor is also designed to be integrated with a Play Framework controller for handling
 * HTTP requests.
 */
public abstract class TagActor extends AbstractActor {
    /**
     * Creates a {@code YouTubeService} instance.
     *
     * <p>This method is meant to be overridden by subclasses to provide a custom implementation for creating
     * the {@code YouTubeService}.
     *
     * @return A {@code YouTubeService} instance.
     * @throws GeneralSecurityException If a security issue occurs during service creation.
     * @throws IOException              If an I/O error occurs during service creation.
     */
    protected abstract YouTubeService createYouTubeService()
            throws GeneralSecurityException, IOException;

    // Nested message classes for actor communication
    public static class SearchVideosRequest {
        private final String tag;

        public SearchVideosRequest(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return tag;
        }
    }
    /**
     * Factory method for creating {@code Props} for this actor.
     *
     * @return A {@code Props} instance for creating a {@code TagActor}.
     */
    // Static factory method for creating Props
    public static Props props() {
        return Props.create(TagActor.class);
    }

    /**
     * Defines the message handling behavior of this actor.
     *
     * <p>Handles:
     * <ul>
     *     <li>{@code SearchVideosRequest}: Initiates a video search for the provided tag.</li>
     * </ul>
     *
     * @return The {@code Receive} instance defining the actor's behavior.
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SearchVideosRequest.class, this::handleSearchRequest)
                .build();
    }

    /**
     * Handles video search requests.
     *
     * <p>Initializes the YouTube service and performs a search for videos matching the provided tag.
     * The search is executed asynchronously, and the results are sent back to the requester.
     *
     * @param request The {@code SearchVideosRequest} containing the search tag.
     */
    private void handleSearchRequest(SearchVideosRequest request) {
        // Sender is the original requester
        final ActorRef sender = getSender();

        // Create a YouTube service
        final YouTubeService youtubeService;
        try {
            youtubeService = new YouTubeService("AIzaSyCij5EdAmn7uMkPq-1Alg6IvvDocQkHpDM");
        } catch (GeneralSecurityException | IOException e) {
            sender.tell(
                    Results.internalServerError("Error initializing YouTube service: " + e.getMessage()),
                    getSelf()
            );
            return;
        }

        // Perform search asynchronously
        CompletableFuture.supplyAsync(() -> {
            try {
                List<VideoData> videos = youtubeService.searchVideos(request.getTag(), 10);
                return views.html.videoList.render(request.getTag(), videos);
            } catch (IOException e) {
                return Results.internalServerError("Error fetching videos: " + e.getMessage());
            }
        }).thenAccept(result -> {
            // Send result back to sender
            sender.tell(result, getSelf());
        });
    }

    /**
     * Handles a video search request in the context of a Play Framework controller.
     *
     * <p>This method uses the {@code TagActor} to perform a video search and returns the result asynchronously
     * as a {@code CompletionStage}.
     *
     * @param system The {@code ActorSystem} used to create the actor.
     * @param tag    The search tag for fetching videos.
     * @return A {@code CompletionStage<Result>} representing the search result.
     * @throws ExecutionException   If an error occurs while fetching the result.
     * @throws InterruptedException If the operation is interrupted.
     */
    // Helper method to create and use the actor in a Play Controller
    public static CompletionStage<Result> searchVideos(ActorSystem system, String tag) throws ExecutionException, InterruptedException {
        // Create a promise/future to return the result
        CompletableFuture<Result> resultFuture = new CompletableFuture<>();

        // Create the actor
        ActorRef searchActor = system.actorOf(TagActor.props());

        // Set up a temporary receiver to handle the response
        ActorRef receiver = system.actorOf(Props.create(AbstractActor.class, () ->
                new AbstractActor() {
                    @Override
                    public Receive createReceive() {
                        return receiveBuilder()
                                .match(Result.class, result -> {
                                    resultFuture.complete(result);
                                    getContext().stop(getSelf());
                                })
                                .match(Status.Failure.class, failure -> {
                                    resultFuture.completeExceptionally(failure.cause());
                                    getContext().stop(getSelf());
                                })
                                .build();
                    }
                }
        ));

        // Send the search request
        searchActor.tell(new SearchVideosRequest(tag), receiver);

        return CompletableFuture.completedFuture(resultFuture.get());
    }
}
