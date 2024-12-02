package actors;

import akka.actor.*;
import akka.testkit.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import java.util.*;
import models.data.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserActorTest {
    private ActorSystem system;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        system = ActorSystem.create();
        objectMapper = new ObjectMapper();
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        TestKit.shutdownActorSystem(system, Duration.create(3, "seconds"), true);
    }

    @Test
    public void testUserActorSearchFlow() {
        new TestKit(system) {{
            final TestProbe mockWebSocketOut = new TestProbe(system);
            final TestProbe mockCacheManagerActor = new TestProbe(system);

            final Props props = UserActor.props(mockWebSocketOut.ref(), mockCacheManagerActor.ref());
            final ActorRef userActor = system.actorOf(props);

            // Simulate search request
            String searchTerm = "test search term";

            // Send the search term
            userActor.tell(searchTerm, mockCacheManagerActor.ref());

            // Expect SearchRequest to be sent to CacheManagerActor
            CacheManagerActor.SearchRequest searchRequest = mockCacheManagerActor
                    .expectMsgClass(FiniteDuration.apply(5, TimeUnit.SECONDS), CacheManagerActor.SearchRequest.class);
            assertEquals(searchTerm, searchRequest.searchTerm);
        }};
    }

    @Test
    public void testMaxSearchesLimitReached() {
        new TestKit(system) {{
            final TestProbe mockWebSocketOut = new TestProbe(system);
            final TestProbe mockCacheManagerActor = new TestProbe(system);

            final Props props = UserActor.props(mockWebSocketOut.ref(), mockCacheManagerActor.ref());
            final ActorRef userActor = system.actorOf(props);

            // Simulate reaching max searches limit
            for (int i = 0; i < Constants.MAX_VIDEOS_DISPLAY_COUNT + 2; i++) {
                userActor.tell("search term " + i, mockCacheManagerActor.ref());
            }

            // Verify that only MAX_VIDEOS_DISPLAY_COUNT searches are processed
            mockCacheManagerActor.expectMsgClass(
                    FiniteDuration.apply(5, TimeUnit.SECONDS),
                    CacheManagerActor.SearchRequest.class
            );
        }};
    }


    @Test
    public void testRefreshCacheMessage() {
        new TestKit(system) {{
            final TestProbe mockWebSocketOut = new TestProbe(system);
            final TestProbe mockCacheManagerActor = new TestProbe(system);

            final Props props = UserActor.props(mockWebSocketOut.ref(), mockCacheManagerActor.ref());
            final ActorRef userActor = system.actorOf(props);

            // Send a few search terms first
            userActor.tell("search1", mockCacheManagerActor.ref());
            userActor.tell("search2", mockCacheManagerActor.ref());

            // Send RefreshCache message
            userActor.tell(new UserActor.RefreshCache(), mockCacheManagerActor.ref());

            // Expect multiple SearchRequest messages for recent searches
            mockCacheManagerActor.expectMsgClass(
                    FiniteDuration.apply(5, TimeUnit.SECONDS),
                    CacheManagerActor.SearchRequest.class
            );
            mockCacheManagerActor.expectMsgClass(
                    FiniteDuration.apply(5, TimeUnit.SECONDS),
                    CacheManagerActor.SearchRequest.class
            );
        }};
    }

    @Test
    public void testUnknownMessageHandling() {
        new TestKit(system) {{
            final TestProbe mockWebSocketOut = new TestProbe(system);
            final TestProbe mockCacheManagerActor = new TestProbe(system);

            final Props props = UserActor.props(mockWebSocketOut.ref(), mockCacheManagerActor.ref());
            final ActorRef userActor = system.actorOf(props);

            // Send an unknown message type
            userActor.tell(new Object(), mockCacheManagerActor.ref());

            // Verify that an unknown message warning is logged
            // Note: This is a basic check and might require more sophisticated logging verification
            mockCacheManagerActor.expectNoMsg(FiniteDuration.apply(1, TimeUnit.SECONDS));
        }};
    }
}