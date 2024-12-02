package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Status;
import akka.testkit.TestKit;
import akka.testkit.TestProbe;
import com.typesafe.config.Config;
import models.api.YouTubeService;
import models.data.ChannelVideoData;
import models.data.Constants;
import models.data.VideoSearchData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import scala.concurrent.duration.Duration;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CacheManagerActorTest {
    private ActorSystem system;
    private YouTubeService youTubeServiceMock;
    private Config configMock;
    private ActorRef cacheManagerActor;

    @Before
    public void setUp() throws GeneralSecurityException, IOException {
        system = ActorSystem.create("testActorSystem");

        // Mock YouTubeService and Config
        youTubeServiceMock = mock(YouTubeService.class);
        configMock = mock(Config.class);
        when(configMock.getString("youtube.api.key")).thenReturn("test-api-key");

        // Create the CacheManagerActor with mocked dependencies
        cacheManagerActor = system.actorOf(
                CacheManagerActor.props(configMock),
                "cacheManagerActor"
        );
    }

    @After
    public void tearDown() {
        TestKit.shutdownActorSystem(system, Duration.create(3, "seconds"), true);
    }

    @Test
    public void testSearchRequestCacheMiss() throws IOException {
        String searchTerm = "test query";
        VideoSearchData mockSearchData = mock(VideoSearchData.class);
        when(mockSearchData.getQuery()).thenReturn(searchTerm);

        // Mock YouTubeService to return mock search data
        when(youTubeServiceMock.searchVideos(searchTerm, Constants.MAX_VIDEOS_DISPLAY_COUNT))
                .thenReturn(Arrays.asList()); // Simulate search results

        // Create a test probe to capture the response
        TestProbe probe = new TestProbe(system);

        // Send a search request
        cacheManagerActor.tell(
                new CacheManagerActor.SearchRequest(searchTerm),
                probe.ref()
        );

        // Verify the response
        probe.expectMsgClass(VideoSearchData.class);
    }

    @Test
    public void testSearchRequestCacheHit() throws IOException {
        String searchTerm = "cached query";
        VideoSearchData mockSearchData = mock(VideoSearchData.class);
        when(mockSearchData.getQuery()).thenReturn(searchTerm);

        // First, populate the cache
        cacheManagerActor.tell(mockSearchData, cacheManagerActor);

        // Create a test probe to capture the response
        TestProbe probe = new TestProbe(system);

        // Send the same search request again
        cacheManagerActor.tell(
                new CacheManagerActor.SearchRequest(searchTerm),
                probe.ref()
        );

        // Verify that the cached result is returned
        VideoSearchData cachedResult = probe.expectMsgClass(VideoSearchData.class);
        assertEquals(searchTerm, cachedResult.getQuery());
    }

   /* @Test
    public void testChannelRequestCacheMiss() throws IOException {
        String channelId = "test-channel-id";
        ChannelVideoData mockChannelVideoData = mock(ChannelVideoData.class);

        // Create a test probe to capture the response
        TestProbe probe = new TestProbe(system);

        // Send a channel request
        cacheManagerActor.tell(
                new CacheManagerActor.ChannelRequest(channelId),
                probe.ref()
        );

        // Verify that a channel search request is processed
        probe.expectMsgClass(ChannelVideoData.class);
    }

    @Test
    public void testChannelRequestCacheHit() throws IOException {
        String channelId = "cached-channel-id";
        ChannelVideoData mockChannelVideoData = mock(ChannelVideoData.class);
        when(mockChannelVideoData.getVideoDataList()).thenReturn(Arrays.asList(
                mock(models.data.VideoData.class)
        ));

        // First, populate the cache
        cacheManagerActor.tell(
                new ChannelProfileActor.SearchChannelVideosResponse(mockChannelVideoData),
                cacheManagerActor
        );

        // Create a test probe to capture the response
        TestProbe probe = new TestProbe(system);

        // Send the same channel request again
        cacheManagerActor.tell(
                new CacheManagerActor.ChannelRequest(channelId),
                probe.ref()
        );

        // Verify that the cached result is returned
        ChannelVideoData cachedResult = probe.expectMsgClass(ChannelVideoData.class);
        assertEquals(mockChannelVideoData, cachedResult);
    }

    @Test
    public void testVideoTagRequestSuccess() throws IOException {
        String videoId = "test-video-id";
        List<String> mockTags = Arrays.asList("tag1", "tag2", "tag3");

        // Mock the YouTubeService to return mock tags
        when(youTubeServiceMock.getVideoTags(videoId)).thenReturn(mockTags);

        // Create a test probe to capture the response
        TestProbe probe = new TestProbe(system);

        // Send a video tag request
        cacheManagerActor.tell(
                new CacheManagerActor.VideoTagRequest(videoId),
                probe.ref()
        );

        // Verify the response contains the expected tags
        List<String> receivedTags = probe.expectMsgClass(List.class);
        assertEquals(mockTags, receivedTags);
    }

    @Test
    public void testVideoTagRequestFailure() throws IOException {
        String videoId = "error-video-id";

        // Mock the YouTubeService to throw an IOException
        when(youTubeServiceMock.getVideoTags(videoId))
                .thenThrow(new IOException("Network error"));

        // Create a test probe to capture the response
        TestProbe probe = new TestProbe(system);

        // Send a video tag request
        cacheManagerActor.tell(
                new CacheManagerActor.VideoTagRequest(videoId),
                probe.ref()
        );

        // Verify that a failure message is sent
        Status.Failure failure = probe.expectMsgClass(Status.Failure.class);
        assertTrue(failure.cause() instanceof IOException);
        assertEquals("Network error", failure.cause().getMessage());
    }
*/
    @Test
    public void testRefreshCache() throws IOException {
        String searchTerm = "refresh-term";
        VideoSearchData mockSearchData = mock(VideoSearchData.class);
        when(mockSearchData.getQuery()).thenReturn(searchTerm);

        // First, populate the cache
        cacheManagerActor.tell(mockSearchData, cacheManagerActor);

        // Create a test probe to capture the response
        TestProbe probe = new TestProbe(system);

        // Trigger cache refresh
        cacheManagerActor.tell(new CacheManagerActor.RefreshCache(), probe.ref());

        // Give some time for the refresh to complete
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Verify that a search request still returns a result
        cacheManagerActor.tell(
                new CacheManagerActor.SearchRequest(searchTerm),
                probe.ref()
        );

        // Verify that the cached result is maintained or updated
        probe.expectMsgClass(VideoSearchData.class);
    }
}