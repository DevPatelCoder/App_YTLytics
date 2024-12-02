package actors;

import akka.actor.ActorSystem;
import akka.actor.ActorRef;
import akka.testkit.TestKit;
import akka.testkit.TestProbe;
import models.api.YouTubeService;
import models.data.Constants;
import models.data.VideoData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Result;
import scala.concurrent.duration.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VideoSearchActorTest {

    private ActorSystem system;
    private YouTubeService youTubeServiceMock;
    private ActorRef videoSearchActor;

    @Before
    public void setUp() {
        system = ActorSystem.create("testActorSystem");
        youTubeServiceMock = mock(YouTubeService.class);
        videoSearchActor = system.actorOf(VideoSearchActor.props(youTubeServiceMock));
    }

    @After
    public void tearDown() {
        // Shutdown the ActorSystem properly
        TestKit.shutdownActorSystem(system, Duration.create(3, "seconds"), true);
    }

    @Test
    public void testSearchVideosSuccess() throws IOException {
        String searchTerm = "cats";

        // Create mock data
        List<VideoData> videoDataList = new ArrayList<>();
        VideoData video1 = mock(VideoData.class);
        VideoData video2 = mock(VideoData.class);
        videoDataList.add(video1);
        videoDataList.add(video2);

        // Set up the mock behavior for the YouTubeService
        when(youTubeServiceMock.searchVideos(searchTerm, Constants.MAX_VIDEOS_DISPLAY_COUNT)).thenReturn(videoDataList);

        // Create a TestProbe to receive the response from the actor
        TestProbe probe = new TestProbe(system);

        // Send a SearchVideosRequest message to the actor
        videoSearchActor.tell(new VideoSearchActor.SearchVideosRequest(searchTerm), probe.ref());

        // Expect a successful response with the list of videos
        VideoSearchActor.SearchVideosResponse response = probe.expectMsgClass(VideoSearchActor.SearchVideosResponse.class);

        // Verify that the response contains the expected videos
        assertTrue("Response should contain 2 videos", response.videos.size() == 2);
        assertTrue("Response should contain video1", response.videos.contains(video1));
        assertTrue("Response should contain video2", response.videos.contains(video2));
    }

    @Test
    public void testSearchVideosNoResults() throws IOException {
        String searchTerm = "nonexistentterm";

        // Set up the mock behavior for the YouTubeService to return an empty list
        when(youTubeServiceMock.searchVideos(searchTerm, Constants.MAX_VIDEOS_DISPLAY_COUNT)).thenReturn(new ArrayList<>());

        // Create a TestProbe to receive the response from the actor
        TestProbe probe = new TestProbe(system);

        // Send a SearchVideosRequest message to the actor
        videoSearchActor.tell(new VideoSearchActor.SearchVideosRequest(searchTerm), probe.ref());

        // Expect a successful response with an empty list of videos
        VideoSearchActor.SearchVideosResponse response = probe.expectMsgClass(VideoSearchActor.SearchVideosResponse.class);

        // Verify that the response contains no videos
        assertTrue("Response should contain no videos", response.videos.isEmpty());
    }

    @Test
    public void testSearchVideosFailure() throws IOException {
        String searchTerm = "cats";

        // Simulate an IOException being thrown by the YouTubeService
        try {
            when(youTubeServiceMock.searchVideos(searchTerm, Constants.MAX_VIDEOS_DISPLAY_COUNT)).thenThrow(new IOException("Network error"));
        } catch (IOException e) {
            e.printStackTrace();  // This exception is already being mocked, so the printstack trace is not needed
        }

        // Create a TestProbe to receive the response from the actor
        TestProbe probe = new TestProbe(system);

        // Send a SearchVideosRequest message to the actor
        videoSearchActor.tell(new VideoSearchActor.SearchVideosRequest(searchTerm), probe.ref());

        // Expect a failure response
        akka.actor.Status.Failure failure = probe.expectMsgClass(akka.actor.Status.Failure.class);

        // Assert that the failure contains the expected error message
        assertTrue("Failure should be caused by IOException", failure.cause() instanceof IOException);
        assertTrue("Failure should contain 'Network error' message", failure.cause().getMessage().contains("Network error"));
    }

}