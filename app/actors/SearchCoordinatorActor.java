package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Status;
import models.api.YouTubeService;
import models.data.Sentiment;
import models.data.VideoData;
import models.data.VideoSearchData;

import java.util.List;

/**
 * The {@code SearchCoordinatorActor} coordinates the search process for a given term.
 * It concurrently retrieves video search results and sentiment analysis data by interacting
 * with its child actors {@code VideoSearchActor} and {@code SentimentActor}.
 *
 * <p>The actor sends the combined result as a {@code VideoSearchData} object to its parent actor.
 * In case of failure, it notifies the parent with an error result for potential caching.
 */
public class SearchCoordinatorActor extends AbstractActor {
    private final String searchTerm;
    private final YouTubeService youTubeService;
    private final ActorRef parentActor;
    private ActorRef originalSender;

    private ActorRef videoSearchActor;
    private ActorRef sentimentActor;
    private ActorRef readabilityActor;
    private List<VideoData> videos;
    private Sentiment sentiment;
    private Double averageGradeLevel;
    private Double averageReadingEase;

    /**
     * Constructs a {@code SearchCoordinatorActor}.
     *
     * @param searchTerm     The term to search for on YouTube.
     * @param youTubeService The service used for YouTube API interactions.
     * @param parentActor    The parent actor to report results or errors to.
     */
    public SearchCoordinatorActor(String searchTerm, YouTubeService youTubeService, ActorRef parentActor) {
        this.searchTerm = searchTerm;
        this.youTubeService = youTubeService;
        this.parentActor = parentActor;
    }

    /**
     * Factory method to create {@code Props} for this actor.
     *
     * @param searchTerm     The term to search for on YouTube.
     * @param youTubeService The service used for YouTube API interactions.
     * @param parentActor    The parent actor to report results or errors to.
     * @return A {@code Props} instance for creating this actor.
     */
    public static Props props(String searchTerm, YouTubeService youTubeService, ActorRef parentActor) {
        return Props.create(SearchCoordinatorActor.class,
                () -> new SearchCoordinatorActor(searchTerm, youTubeService, parentActor));
    }

    /**
     * Lifecycle method called before the actor starts.
     *
     * <p>Initializes child actors for video search and sentiment analysis,
     * and sends requests to both in parallel.
     */
    @Override
    public void preStart() {
        // Initialize child actors
        videoSearchActor = getContext().actorOf(
                VideoSearchActor.props(youTubeService),
                "video-search-" + searchTerm.hashCode()
        );
        sentimentActor = getContext().actorOf(
                SentimentActor.props(youTubeService),
                "sentiment-" + searchTerm.hashCode()
        );
        readabilityActor = getContext().actorOf(
                ReadabilityActor.props(youTubeService),
                "readability-" + searchTerm.hashCode()
        );

        // Send both requests in parallel
        videoSearchActor.tell(new VideoSearchActor.SearchVideosRequest(searchTerm), getSelf());
        sentimentActor.tell(new SentimentActor.AnalyzeSentimentRequest(searchTerm), getSelf());
        readabilityActor.tell(new ReadabilityActor.AnalyzeReadabilityRequest(searchTerm), getSelf());
    }

    /**
     * Lifecycle method called when the actor stops.
     *
     * <p>Stops the child actors created during the actor's lifecycle.
     */
    @Override
    public void postStop() {
        // Clean up child actors
        if (videoSearchActor != null) {
            getContext().stop(videoSearchActor);
        }
        if (sentimentActor != null) {
            getContext().stop(sentimentActor);
        }
        if (readabilityActor != null) {
            getContext().stop(readabilityActor);
        }
    }

    /**
     * Defines the message handling behavior of this actor.
     *
     * <p>Handles:
     * <ul>
     *     <li>{@code ActorRef}: Stores the reference to the original sender.</li>
     *     <li>{@code VideoSearchActor.SearchVideosResponse}: Processes video search results.</li>
     *     <li>{@code SentimentActor.AnalyzeSentimentResponse}: Processes sentiment analysis results.</li>
     *     <li>{@code Status.Failure}: Handles errors and notifies the parent actor.</li>
     * </ul>
     *
     * @return The {@code Receive} instance defining the actor's behavior.
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ActorRef.class, sender -> {
                    // Store the original sender when received from parent
                    this.originalSender = sender;
                })
                .match(VideoSearchActor.SearchVideosResponse.class, this::handleVideoResponse)
                .match(SentimentActor.AnalyzeSentimentResponse.class, this::handleSentimentResponse)
                .match(ReadabilityActor.AnalyzeReadabilityResponse.class, this::handleReadabilityResponse)
                .match(Status.Failure.class, this::handleFailure)
                .build();
    }

    /**
     * Handles the video search results received from {@code VideoSearchActor}.
     *
     * @param response The video search response containing a list of videos.
     */
    private void handleVideoResponse(VideoSearchActor.SearchVideosResponse response) {
        this.videos = response.videos;
        checkCompletion();
    }

    /**
     * Handles the sentiment analysis results received from {@code SentimentActor}.
     *
     * @param response The sentiment analysis response containing sentiment data.
     */
    private void handleSentimentResponse(SentimentActor.AnalyzeSentimentResponse response) {
        this.sentiment = response.sentiment;
        checkCompletion();
    }

    private void handleReadabilityResponse(ReadabilityActor.AnalyzeReadabilityResponse response) {
        this.averageGradeLevel = response.averageGrade;
        this.averageReadingEase = response.averageScore;
        checkCompletion();
    }

    /**
     * Handles failure messages and reports an error result to the parent actor.
     *
     * @param failure The failure message containing the exception.
     */
    private void handleFailure(Status.Failure failure) {
        VideoSearchData errorResult = new VideoSearchData(searchTerm, null, null, 0.0, 0.0, null);
        // Send error result to parent actor for potential error caching
        parentActor.tell(errorResult, originalSender);
        getContext().stop(getSelf());
    }

    /**
     * Checks if both video search and sentiment analysis results are available.
     *
     * <p>If both are available, sends the combined result to the parent actor and stops itself.
     */
    private void checkCompletion() {
        if (videos != null && sentiment != null) {
            VideoSearchData result = new VideoSearchData(searchTerm, videos, sentiment, averageGradeLevel, averageReadingEase, null);
            // Send result to parent actor for caching
            parentActor.tell(result, originalSender);
            getContext().stop(getSelf());
        }
    }

}