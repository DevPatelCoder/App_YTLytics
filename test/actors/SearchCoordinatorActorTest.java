package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Status;
import akka.testkit.javadsl.TestKit;
import models.api.YouTubeService;
import models.data.Sentiment;
import models.data.VideoData;
import models.data.VideoSearchData;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class SearchCoordinatorActorTest {
    private static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create("SearchCoordinatorActorTest");
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testFailureResponse() {
        new TestKit(system) {{
            // Mock dependencies
            YouTubeService mockYouTubeService = mock(YouTubeService.class);

            String searchTerm = "test failure";
            ActorRef parentActor = getTestActor();

            // Create SearchCoordinatorActor
            Props props = SearchCoordinatorActor.props(searchTerm, mockYouTubeService, parentActor);
            ActorRef searchCoordinatorActor = system.actorOf(props);

            // Simulate a failure response
            Exception failureCause = new RuntimeException("Failure during processing");
            searchCoordinatorActor.tell(new Status.Failure(failureCause), ActorRef.noSender());

            // Assert that the message received matches the expected structure
            VideoSearchData actualErrorResult = expectMsgClass(VideoSearchData.class);

            // Assertions
            assertEquals(searchTerm, actualErrorResult.getQuery());
            assertNull(actualErrorResult.getVideos());
            assertNull(actualErrorResult.getSentiment());
//            assertNull(actualErrorResult.getAvgFleshGrad());
//            assertNull(actualErrorResult.getAvgFleshScore());
            assertNull(actualErrorResult.getWordCount()); // Assuming word count is null in a failure scenario
        }};
    }}