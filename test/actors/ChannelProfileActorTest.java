package actors;

import akka.actor.*;
import akka.testkit.TestKit;
import akka.testkit.TestProbe;
import models.data.ChannelVideoData;
import models.api.YouTubeService;
import models.data.Constants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import models.data.*;
import scala.concurrent.duration.Duration;

public class ChannelProfileActorTest {

    private ActorSystem system;
    private YouTubeService youTubeServiceMock;
    private ActorRef channelProfileActor;

    @Before
    public void setUp() {
        system = ActorSystem.create("testActorSystem");
        youTubeServiceMock = mock(YouTubeService.class);
        channelProfileActor = system.actorOf(ChannelProfileActor.props(youTubeServiceMock, ActorRef.noSender()));
    }

    @After
    public void tearDown() {
        TestKit.shutdownActorSystem(system, Duration.create(3, "seconds"), true);
    }

    @Test
    public void testGetChannelProfileSuccess() throws IOException {
        String channelId = "validChannelId";

        // Mocking the data returned by the YouTubeService
        List<VideoData> videoDataList = new ArrayList<>();
        ChannelMetaData channelMetaData = mock(ChannelMetaData.class);
        ChannelVideoData mockChannelData = new ChannelVideoData(videoDataList, channelMetaData);

        // Mocking the YouTubeService to return the mocked ChannelVideoData
        when(youTubeServiceMock.getChannelRecentVideos(channelId, Constants.MAX_VIDEOS_DISPLAY_COUNT))
                .thenReturn(mockChannelData);

        // Create a TestProbe to receive the response from the actor
        TestProbe probe = new TestProbe(system);

        // Send the SearchChannelVideosRequest to the actor
        channelProfileActor.tell(new ChannelProfileActor.SearchChannelVideosRequest(channelId), probe.ref());

        // Expect a successful result
        ChannelProfileActor.SearchChannelVideosResponse response = probe.expectMsgClass(ChannelProfileActor.SearchChannelVideosResponse.class);

        // Verify that the response contains the expected data
        assertTrue(response.channelVideoData == mockChannelData);
    }

    @Test
    public void testChannelNotFound() throws IOException {
        String channelId = "invalidChannelId";

        // Mocking the service to throw an exception for invalid channelId
        when(youTubeServiceMock.getChannelRecentVideos(channelId, Constants.MAX_VIDEOS_DISPLAY_COUNT))
                .thenThrow(new IOException("Channel not found"));

        // Create a TestProbe to receive the response from the actor
        TestProbe probe = new TestProbe(system);

        // Send the SearchChannelVideosRequest to the actor
        channelProfileActor.tell(new ChannelProfileActor.SearchChannelVideosRequest(channelId), probe.ref());

        // Expect a failure response
        probe.expectMsgClass(Status.Failure.class);
    }

    @Test
    public void testErrorDuringDataRetrieval() throws IOException {
        String channelId = "errorChannelId";

        // Simulate an IOException in the YouTubeService
        when(youTubeServiceMock.getChannelRecentVideos(channelId, Constants.MAX_VIDEOS_DISPLAY_COUNT))
                .thenThrow(new IOException("IO Exception"));

        // Create a TestProbe to receive the response from the actor
        TestProbe probe = new TestProbe(system);

        // Send the SearchChannelVideosRequest to the actor
        channelProfileActor.tell(new ChannelProfileActor.SearchChannelVideosRequest(channelId), probe.ref());

        // Expect a failure response
        Status.Failure failure = probe.expectMsgClass(Status.Failure.class);

        // Assert that the failure contains the expected error message
        assertTrue(failure.cause() instanceof IOException);
        assertTrue(failure.cause().getMessage().contains("IO Exception"));
    }
}
