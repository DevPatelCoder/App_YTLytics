package actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.Status;
import models.api.YouTubeService;
import models.data.Constants;

import java.io.IOException;

/**
 * The {@code ReadabilityActor} is responsible for calculating readability scores and grades for YouTube video descriptions.
 *
 * <p>This actor communicates with the {@code YouTubeService} to fetch and analyze video descriptions for readability metrics
 * based on a provided search term. It handles incoming requests for readability analysis and responds with the results or errors.
 */
public class ReadabilityActor extends AbstractActor {
    private final YouTubeService youTubeService;

    /**
     * Constructs a {@code ReadabilityActor}.
     *
     * @param youTubeService The service used to perform readability analysis on YouTube data.
     */
    public ReadabilityActor(YouTubeService youTubeService) {
        this.youTubeService = youTubeService;
    }

    /**
     * Factory method to create {@code Props} for this actor.
     *
     * @param youTubeService The service used to perform readability analysis.
     * @return A {@code Props} instance for creating this actor.
     */
    public static Props props(YouTubeService youTubeService) {
        return Props.create(ReadabilityActor.class, () -> new ReadabilityActor(youTubeService));
    }

    /**
     * Defines the message handling behavior of this actor.
     *
     * <p>Handles:
     * <ul>
     *     <li>{@code AnalyzeReadabilityRequest}: Triggers readability analysis for the provided search term.</li>
     * </ul>
     *
     * @return The {@code Receive} instance defining the actor's behavior.
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(AnalyzeReadabilityRequest.class, this::handleReadabilityAnalysis)
                .build();
    }

    /**
     * Handles readability analysis requests.
     *
     * <p>Uses the {@code YouTubeService} to calculate readability scores and grades for video descriptions related
     * to the search term. Sends the result back to the sender or reports a failure if an exception occurs.
     *
     * @param request The request containing the search term for readability analysis.
     */
    private void handleReadabilityAnalysis(AnalyzeReadabilityRequest request) {
        try {
            double averageGrade = youTubeService.getavgFleshGrade(request.searchTerm, Constants.MAX_DESC_SENTIMENT_COUNT);
            double averageScore = youTubeService.getavgFleshScore(request.searchTerm, Constants.MAX_DESC_SENTIMENT_COUNT);

            getSender().tell(new AnalyzeReadabilityResponse(averageGrade, averageScore), getSelf());
        } catch (IOException e) {
            getSender().tell(new Status.Failure(e), getSelf());
        }
    }

    /**
     * Represents a request to perform readability analysis.
     */
    public static class AnalyzeReadabilityRequest {
        /**
         * The search term for which readability analysis is to be performed.
         */
        public final String searchTerm;

        /**
         * Constructs an {@code AnalyzeReadabilityRequest}.
         *
         * @param searchTerm The search term for readability analysis.
         */
        public AnalyzeReadabilityRequest(String searchTerm) {
            this.searchTerm = searchTerm;
        }
    }

    /**
     * Represents the response containing the readability analysis results.
     */
    public static class AnalyzeReadabilityResponse {
        /**
         * The average Flesch-Kincaid Grade Level.
         */
        public final double averageGrade;

        /**
         * The average Flesch Reading Ease score.
         */
        public final double averageScore;

        /**
         * Constructs an {@code AnalyzeReadabilityResponse}.
         *
         * @param averageGrade The average Flesch-Kincaid Grade Level.
         * @param averageScore The average Flesch Reading Ease score.
         */
        public AnalyzeReadabilityResponse(double averageGrade, double averageScore) {
            this.averageGrade = averageGrade;
            this.averageScore = averageScore;
        }
    }
}
