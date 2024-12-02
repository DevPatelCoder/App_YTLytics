package actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.Status;
import models.api.YouTubeService;
import models.data.Constants;
import models.data.Sentiment;

import java.io.IOException;

/**
 * The {@code SentimentActor} is responsible for performing sentiment analysis on YouTube data.
 *
 * <p>This actor communicates with the {@code YouTubeService} to fetch and analyze the sentiment of video descriptions
 * based on a provided search term. It handles incoming requests for sentiment analysis and responds with the results or errors.
 */
public class SentimentActor extends AbstractActor {
    private final YouTubeService youTubeService;

    /**
     * Constructs a {@code SentimentActor}.
     *
     * @param youTubeService The service used to perform sentiment analysis on YouTube data.
     */
    public SentimentActor(YouTubeService youTubeService) {
        this.youTubeService = youTubeService;
    }

    /**
     * Factory method to create {@code Props} for this actor.
     *
     * @param youTubeService The service used to perform sentiment analysis.
     * @return A {@code Props} instance for creating this actor.
     */
    public static Props props(YouTubeService youTubeService) {
        return Props.create(SentimentActor.class, () -> new SentimentActor(youTubeService));
    }

    /**
     * Defines the message handling behavior of this actor.
     *
     * <p>Handles:
     * <ul>
     *     <li>{@code AnalyzeSentimentRequest}: Triggers sentiment analysis for the provided search term.</li>
     * </ul>
     *
     * @return The {@code Receive} instance defining the actor's behavior.
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(AnalyzeSentimentRequest.class, this::handleSentimentAnalysis)
                .build();
    }

    /**
     * Handles sentiment analysis requests.
     *
     * <p>Uses the {@code YouTubeService} to perform sentiment analysis on video descriptions related to the search term.
     * Sends the result back to the sender or reports a failure if an exception occurs.
     *
     * @param request The request containing the search term for sentiment analysis.
     */
    private void handleSentimentAnalysis(AnalyzeSentimentRequest request) {
        try {
            Sentiment sentiment = youTubeService.getSentimentalAnalysis(
                    request.searchTerm,
                    Constants.MAX_DESC_SENTIMENT_COUNT
            );
            getSender().tell(new AnalyzeSentimentResponse(sentiment), getSelf());
        } catch (IOException e) {
            getSender().tell(new Status.Failure(e), getSelf());
        }
    }

    /**
     * Represents a request to perform sentiment analysis.
     */
    public static class AnalyzeSentimentRequest {
        /**
         * The search term for which sentiment analysis is to be performed.
         */
        public final String searchTerm;
        /**
         * Constructs an {@code AnalyzeSentimentRequest}.
         *
         * @param searchTerm The search term for sentiment analysis.
         */
        public AnalyzeSentimentRequest(String searchTerm) {
            this.searchTerm = searchTerm;
        }
    }
    /**
     * Represents the response containing the sentiment analysis result.
     */
    public static class AnalyzeSentimentResponse {
        /**
         * The result of the sentiment analysis.
         */
        public final Sentiment sentiment;
        /**
         * Constructs an {@code AnalyzeSentimentResponse}.
         *
         * @param sentiment The sentiment analysis result.
         */
        public AnalyzeSentimentResponse(Sentiment sentiment) {
            this.sentiment = sentiment;
        }
    }
}
