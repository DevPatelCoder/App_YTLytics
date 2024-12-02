package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.TestKit;
import akka.testkit.TestProbe;
import models.api.YouTubeService;
import models.data.Sentiment;
import models.data.Constants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import scala.concurrent.duration.Duration;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SentimentActorTest {

    private ActorSystem system;
    private YouTubeService youTubeServiceMock;
    private ActorRef sentimentActor;

    @Before
    public void setUp() {
        system = ActorSystem.create("testActorSystem");
        youTubeServiceMock = mock(YouTubeService.class);
        sentimentActor = system.actorOf(SentimentActor.props(youTubeServiceMock));
    }

    @After
    public void tearDown() {
        TestKit.shutdownActorSystem(system, Duration.create(3, "seconds"), true);
    }

    @Test
    public void testSentimentAnalysisSuccess() throws IOException {
        // Define the search term for sentiment analysis
        String searchTerm = "test search term";

        // Setup the mock to return a predefined sentiment (e.g., HAPPY)
        Sentiment mockSentiment = Sentiment.HAPPY;

        // Setup the mock to return the sentiment for this search term
        when(youTubeServiceMock.getSentimentalAnalysis(eq(searchTerm), eq(Constants.MAX_DESC_SENTIMENT_COUNT)))
                .thenReturn(mockSentiment);

        // Create a TestProbe to receive the response from the actor
        TestProbe probe = new TestProbe(system);

        // Send a sentiment analysis request to the actor
        sentimentActor.tell(new SentimentActor.AnalyzeSentimentRequest(searchTerm), probe.ref());

        // Expect a successful sentiment analysis response
        SentimentActor.AnalyzeSentimentResponse response = probe.expectMsgClass(SentimentActor.AnalyzeSentimentResponse.class);

        // Verify that the sentiment analysis response contains the expected sentiment
        assertNotNull(response);
        assertTrue(response.sentiment == Sentiment.HAPPY); // Check if the sentiment is HAPPY
    }

    @Test
    public void testSentimentAnalysisFailure() throws IOException {
        // Define the search term for sentiment analysis
        String searchTerm = "test search term";

        // Simulate an IOException from the YouTubeService
        when(youTubeServiceMock.getSentimentalAnalysis(eq(searchTerm), eq(Constants.MAX_DESC_SENTIMENT_COUNT)))
                .thenThrow(new IOException("Failed to fetch sentiment"));

        // Create a TestProbe to receive the response from the actor
        TestProbe probe = new TestProbe(system);

        // Send a sentiment analysis request to the actor
        sentimentActor.tell(new SentimentActor.AnalyzeSentimentRequest(searchTerm), probe.ref());

        // Expect a failure response from the actor
        akka.actor.Status.Failure failure = probe.expectMsgClass(akka.actor.Status.Failure.class);

        // Verify that the failure contains the expected exception message
        assertTrue(failure.cause() instanceof IOException);
        assertTrue(failure.cause().getMessage().contains("Failed to fetch sentiment"));
    }
}
