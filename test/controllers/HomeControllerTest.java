package controllers;

import Helper.DataHelper;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import models.api.CacheManager;
import models.data.ChannelMetaData;
import models.data.ChannelVideoData;
import models.data.VideoData;
import models.data.WordStatData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Http.Session;
import play.mvc.Result;
import play.test.WithApplication;
import java.math.BigInteger;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.concurrent.CompletionStage;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static play.test.Helpers.*;
import org.mockito.Mockito;
import akka.actor.Props;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
/**
 * Unit tests for the HomeController class.
 * This class tests the methods of the HomeController to ensure correct behavior.
 */
@RunWith(MockitoJUnitRunner.class)
public class HomeControllerTest extends WithApplication {

    private static final Integer OK = 200;
    public static final String SESSION_KEY = "searchedTerm";

    // Changed from static to instance variable
    private HomeController homeController;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private ActorSystem actorSystem;

    @Mock
    private Materializer materializer;

    @Mock
    private com.typesafe.config.Config config;

    @Mock
    private akka.actor.ActorRef cacheManagerActor;

    @Before
    public void setUp() throws GeneralSecurityException, IOException {
        MockitoAnnotations.openMocks(this);

        // Mock the actor creation in the constructor
        when(actorSystem.actorOf(
                Mockito.any(Props.class)  // Use any() to match any Props
        )).thenReturn(cacheManagerActor);

        // Initialize homeController before each test
        homeController = new HomeController(config, cacheManager, actorSystem, materializer);
    }

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void testSearchWebSocket() throws GeneralSecurityException, IOException {
        // Create the WebSocket
        play.mvc.WebSocket webSocket = homeController.searchWebSocket();

        // Verify that a WebSocket is created
        assertNotNull(webSocket);

        // Verify that the CacheManagerActor was created
        verify(actorSystem).actorOf(Mockito.any(Props.class));

        // Verify that the supervisor actor was created
        verify(actorSystem).actorOf(
                Mockito.any(Props.class),
                Mockito.eq("search-supervisor")
        );
    }

    @Test
    public void testIndexWithEmptySession() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/");

        Result result = route(app, request);
        assertEquals(OK.longValue(), result.status());
    }

    @Test
    public void testClearHistory() {
        Http.Request request = mock(Http.Request.class); // Mock the request
        when(request.session()).thenReturn(new Session()); // Mock session if necessary

        // Call the clearHistory method
        CompletionStage<Result> resultFuture = homeController.clearHistory(request);
        Result result = resultFuture.toCompletableFuture().join();

        // Assert the redirect and check that the session key is removed
        assertEquals(303, result.status()); // 303 indicates a redirect
        assertTrue(!result.session().get(SESSION_KEY).isPresent()); // Ensure the session key was removed
    }

    @Test
    public void testGetWordStats() throws IOException {
        // Mock query and WordStatData
        String query = "testQuery";
        WordStatData mockWordStatData = mock(WordStatData.class);

        // Mock the cacheManager to return the mocked WordStatData
        when(cacheManager.getWordStats(query)).thenReturn(mockWordStatData);

        // Call the getWordStats method
        CompletionStage<Result> resultFuture = homeController.getWordStats(query);
        Result result = resultFuture.toCompletableFuture().join();

        // Assert the result status and verify correct rendering
        assertEquals(OK.longValue(), result.status());
        verify(cacheManager).getWordStats(query);
    }

    @Test
    public void testChannelProfileWithInvalidChannelId() throws IOException {
        String invalidChannelId = "invalidChannelId";
        Http.Request request = DataHelper.getHttpRequest(invalidChannelId);

        // Mock the cacheManager's getChannelResults call to return null
        when(cacheManager.getChannelResults(invalidChannelId)).thenReturn(null);

        // Call the method and join the result
        CompletionStage<Result> resultFuture = homeController.channelProfile(invalidChannelId, request);
        Result result = resultFuture.toCompletableFuture().join();

        // Assert the result status
        assertEquals(500, result.status());
    }

    @Test
    public void testChannelProfileWithIOException() throws IOException {
        String channelId = "channelWithIOException";
        Http.Request request = DataHelper.getHttpRequest(channelId);

        when(cacheManager.getChannelResults(channelId)).thenThrow(new IOException("Test IO Exception"));

        CompletionStage<Result> resultFuture = homeController.channelProfile(channelId, request);
        Result result = resultFuture.toCompletableFuture().join();

        assertEquals(500, result.status());
        assertTrue(contentAsString(result).contains("An error occurred while retrieving channel data."));
        verify(cacheManager).getChannelResults(channelId);
    }

    @Test
    public void testChannelProfileWithValidChannelId() throws IOException {
        String validChannelId = "validChannelId";
        Http.Request request = DataHelper.getHttpRequest(validChannelId);

        // Create mock ChannelMetaData
        ChannelMetaData mockChannelMetaData = new ChannelMetaData();
        mockChannelMetaData.setTitle("Sample Channel");
        mockChannelMetaData.setDescription("This is a sample channel description.");
        mockChannelMetaData.setViewCount(BigInteger.valueOf(1000));
        mockChannelMetaData.setSubscriberCount(BigInteger.valueOf(500));
        mockChannelMetaData.setVideoCount(BigInteger.valueOf(10));
        mockChannelMetaData.setVideoIds(List.of("video1", "video2"));

        // Create mock ChannelVideoData
        ChannelVideoData mockData = mock(ChannelVideoData.class);
        when(mockData.getChannelData()).thenReturn(mockChannelMetaData);
        when(mockData.getVideoDataList()).thenReturn(List.of(
                new VideoData("videoId", "title", "description", "thumbnailUrl", "publishedAt", "channelId")
        ));

        // Mock cacheManager behavior
        when(cacheManager.getChannelResults(validChannelId)).thenReturn(mockData);

        // Call the controller method
        CompletionStage<Result> resultFuture = homeController.channelProfile(validChannelId, request);
        Result result = resultFuture.toCompletableFuture().join();

        // Validate the response
        assertEquals(200, result.status());
        verify(cacheManager).getChannelResults(validChannelId);
    }

    @Test
    public void testChannelProfileWithGenericException() throws IOException {
        String channelId = "channelWithGenericException";
        Http.Request request = DataHelper.getHttpRequest(channelId);

        when(cacheManager.getChannelResults(channelId)).thenThrow(new RuntimeException("Generic Exception"));

        CompletionStage<Result> resultFuture = homeController.channelProfile(channelId, request);
        Result result = resultFuture.toCompletableFuture().join();

        assertEquals(500, result.status());
        assertTrue(contentAsString(result).contains("An unexpected error occurred: Generic Exception"));
        verify(cacheManager).getChannelResults(channelId);
    }

    @Test
    public void testGetWordStatsIOException() throws IOException {
        // Mock query
        String query = "testQuery";

        // Mock the cacheManager to throw an IOException
        when(cacheManager.getWordStats(query)).thenThrow(new IOException("Test IOException"));

        try {
            // Call the getWordStats method
            CompletionStage<Result> resultFuture = homeController.getWordStats(query);
            Result result = resultFuture.toCompletableFuture().join();

            fail("Exception was expected but not thrown");
        } catch (CompletionException e) {
            // Verify that the exception contains the expected cause
            assertTrue(e.getCause() instanceof IOException);
            assertEquals("Test IOException", e.getCause().getMessage());
        }
        // Verify that the cacheManager was called
        verify(cacheManager).getWordStats(query);
    }

    @Test
    public void testShowTagsSuccess() throws IOException {
        // Mock videoId and video tags
        String videoId = "testVideoId";
        List<String> mockTags = List.of("tag1", "tag2", "tag3");

        // Mock the cacheManager to return the mockTags
        when(cacheManager.getVideoTags(videoId)).thenReturn(mockTags);

        // Call the showTags method
        CompletionStage<Result> resultFuture = homeController.showTags(videoId);
        Result result = resultFuture.toCompletableFuture().join();

        // Assert the result status and verify correct tags are returned
        assertEquals(OK.longValue(), result.status());
        verify(cacheManager).getVideoTags(videoId);
    }

    @Test
    public void testShowTagsIOException() throws IOException {
        String videoId = "testVideoId";

        when(cacheManager.getVideoTags(videoId)).thenThrow(new IOException("Test IOException"));

        CompletionStage<Result> resultFuture = homeController.showTags(videoId);
        Result result = resultFuture.toCompletableFuture().join();

        assertEquals(500, result.status());
        verify(cacheManager).getVideoTags(videoId);
    }
}