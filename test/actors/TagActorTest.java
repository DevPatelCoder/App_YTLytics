package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import models.api.YouTubeService;
import models.data.VideoData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import play.mvc.Result;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static akka.pattern.Patterns.ask;

public class TagActorTest {
    private ActorSystem system;

    @BeforeEach
    public void setup() {
        system = ActorSystem.create("TestSystem");
    }

    @AfterEach
    public void teardown() {
        TestKit.shutdownActorSystem(system);
    }

    // Mock YouTube Service for testing
    private static class MockYouTubeService extends YouTubeService {
        private final boolean shouldThrowException;
        private final List<VideoData> mockVideos;

        public MockYouTubeService(boolean shouldThrowException, List<VideoData> mockVideos)
                throws GeneralSecurityException, IOException {
            super("MOCK_API_KEY");
            this.shouldThrowException = shouldThrowException;
            this.mockVideos = mockVideos;
        }

        @Override
        public List<VideoData> searchVideos(String tag, int maxResults) throws IOException {
            if (shouldThrowException) {
                throw new IOException("Simulated YouTube Service Error");
            }
            return mockVideos;
        }
    }

    // Custom TagActor for testing that uses MockYouTubeService
    private static class TestTagActor extends TagActor {
        private final boolean shouldThrowException;
        private final List<VideoData> mockVideos;

        public TestTagActor(boolean shouldThrowException, List<VideoData> mockVideos) {
            this.shouldThrowException = shouldThrowException;
            this.mockVideos = mockVideos;
        }

        @Override
        protected YouTubeService createYouTubeService()
                throws GeneralSecurityException, IOException {
            return new MockYouTubeService(shouldThrowException, mockVideos);
        }
    }

    @Test
    public void testSuccessfulVideoSearch() throws Exception {
        // Prepare mock video data
        List<VideoData> mockVideos = Arrays.asList(
                new VideoData("id1", "Title 1", "Description 1", "thumbnail1"),
                new VideoData("id2", "Title 2", "Description 2", "thumbnail2")
        );

        // Create test kit
        new TestKit(system) {{
            // Create actor with mock service
            ActorRef actor = system.actorOf(
                    Props.create(TestTagActor.class, false, mockVideos)
            );

            // Create search request
            TagActor.SearchVideosRequest request =
                    new TagActor.SearchVideosRequest("testTag");

            // Send request and expect result
            actor.tell(request, getTestActor());

            // Expect a Result object
            expectMsgClass(play.mvc.Result.class);
        }};
    }

    @Test
    public void testVideoSearchWithServiceError() throws Exception {
        new TestKit(system) {{
            // Create actor that will throw an exception
            ActorRef actor = system.actorOf(
                    Props.create(TestTagActor.class, true, null)
            );

            // Create search request
            TagActor.SearchVideosRequest request =
                    new TagActor.SearchVideosRequest("testTag");

            // Send request and expect an error result
            actor.tell(request, getTestActor());

            // Expect an internal server error result
            Result result = expectMsgClass(play.mvc.Result.class);
            assertEquals(500, result.status(),
                    "Should return 500 Internal Server Error");
        }};
    }

    @Test
    public void testSearchVideosStaticMethod() throws Exception {
        // Prepare mock video data
        List<VideoData> mockVideos = Arrays.asList(
                new VideoData("id1", "Title 1", "Description 1", "thumbnail1")
        );

        // Call the static searchVideos method
        CompletionStage<Result> resultStage =
                TagActor.searchVideos(system, "testTag");

        // Get the result with timeout
        Result result = resultStage.toCompletableFuture()
                .get(10, TimeUnit.SECONDS);

        // Assert the result
        assertNotNull(result, "Result should not be null");
        assertTrue(result.status() == 200 || result.status() == 500,
                "Result should be either 200 or 500");
    }

    @Test
    public void testSearchRequestWithEmptyTag() throws Exception {
        new TestKit(system) {{
            // Create actor with mock service
            ActorRef actor = system.actorOf(
                    Props.create(TestTagActor.class, false, Arrays.asList())
            );

            // Create search request with empty tag
            TagActor.SearchVideosRequest request =
                    new TagActor.SearchVideosRequest("");

            // Send request and expect a result
            actor.tell(request, getTestActor());

            // Expect a Result object
            Result result = expectMsgClass(play.mvc.Result.class);
            assertNotNull(result, "Result should not be null");
        }};
    }
}