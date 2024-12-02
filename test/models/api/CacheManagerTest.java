package models.api;

import models.data.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import static Helper.DataHelper.getVideoData;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the CacheManager class. These tests ensure that the CacheManager
 * handles caching behavior for YouTube data, including search results, channel results,
 * word statistics, video tags, and more.
 *
 */
public class CacheManagerTest {

    public static CacheManager cacheManager;

    @Mock
    private YouTubeService service;

    @Mock
    private com.typesafe.config.Config mockConfig;

    private static final String TEST_CHANNEL = "testChannel";

    /**
     * Sets up the necessary preconditions for the tests.
     * This method is called before each test and initializes the CacheManager
     * with a mock configuration and service.
     *
     * @throws GeneralSecurityException if there is an issue with security during setup
     * @throws IOException if an I/O exception occurs during setup
     */
    @Before
    public void setUp() throws GeneralSecurityException, IOException {
        MockitoAnnotations.openMocks(this);
        when(mockConfig.getString("youtube.api.key")).thenReturn("dummy-api-key");
        cacheManager = new CacheManager(mockConfig) {
            @Override
            protected YouTubeService createYouTubeService(String apiKey) {
                return service;
            }
        };

        try {
            Field field = CacheManager.class.getDeclaredField("channelMetaDataHashMap");
            field.setAccessible(true);
            field.set(cacheManager, new ConcurrentHashMap<>());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to set up the test: " + e.getMessage());
        }
    }

    
    /**
     * Tests that channel results are correctly fetched from the cache when the channel exists.
     *
     * @throws IOException if an I/O exception occurs during the test
     */
    @Test
    public void testGetChannelResults_ChannelExistsInCache() throws IOException {
        List<VideoData> videos = getVideoData();
        ChannelMetaData metaData = new ChannelMetaData();
        ChannelVideoData expectedData = new ChannelVideoData(videos, metaData);
        injectCacheEntry(TEST_CHANNEL, expectedData);

        ChannelVideoData result = cacheManager.getChannelResults(TEST_CHANNEL);

        assertNotNull(result);
        assertEquals(expectedData, result);
    }

    /**
     * Tests that channel results are fetched and added to the cache when the channel is not already cached.
     *
     * @throws IOException if an I/O exception occurs during the test
     */
    @Test
    public void testGetChannelResults_ChannelNotInCache_FetchAndAdd() throws IOException {
        List<VideoData> videos = getVideoData();
        ChannelMetaData metaData = new ChannelMetaData();
        ChannelVideoData fetchedData = new ChannelVideoData(videos, metaData);

        when(service.getChannelRecentVideos(TEST_CHANNEL, Constants.MAX_VIDEOS_DISPLAY_COUNT)).thenReturn(fetchedData);

        ChannelVideoData result = cacheManager.getChannelResults(TEST_CHANNEL);

        assertNotNull(result);
        assertEquals(fetchedData, result);
        verify(service, times(1)).getChannelRecentVideos(TEST_CHANNEL, Constants.MAX_VIDEOS_DISPLAY_COUNT);
    }

    /**
     * Tests the scenario when a channel is not in the cache and fetching the data fails.
     *
     * @throws IOException if an I/O exception occurs during the test
     */
    @Test
    public void testGetChannelResults_ChannelNotInCache_And_FetchFails() throws IOException {
        String nonExistingChannel = "nonExistingChannel";

        when(service.getChannelRecentVideos(nonExistingChannel, Constants.MAX_VIDEOS_DISPLAY_COUNT)).thenReturn(null);

        ChannelVideoData result = cacheManager.getChannelResults(nonExistingChannel);

        assertNull(result);
        verify(service, times(1)).getChannelRecentVideos(nonExistingChannel, Constants.MAX_VIDEOS_DISPLAY_COUNT);
    }

    /**
     * Helper method to inject a cache entry for a channel.
     *
     * @param channel the channel name
     * @param data the channel data to cache
     */
    private void injectCacheEntry(String channel, ChannelVideoData data) {
        try {
            Field field = CacheManager.class.getDeclaredField("channelMetaDataHashMap");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            ConcurrentHashMap<String, ChannelVideoData> cache = (ConcurrentHashMap<String, ChannelVideoData>) field.get(cacheManager);
            cache.put(channel, data);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to inject cache entry: " + e.getMessage());
        }
    }

   
    /**
     * Tests the cache hit scenario for fetching word statistics. This test verifies that
     * when word statistics for a given search term are already available in the cache,
     * the data is retrieved from the cache without making a service call.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testGetWordStats_CacheHit() throws Exception {
        // Arrange
        String searchTerm = "testQuery";
        Map<String, Integer> wordCount = new HashMap<>();
        wordCount.put("test", 2);
        wordCount.put("query", 3);

        VideoSearchData cachedData = new VideoSearchData(searchTerm, null, null, 0.0, 0.0, wordCount);

        // Use reflection to populate searchResults
        Field searchResultsField = CacheManager.class.getDeclaredField("searchResults");
        searchResultsField.setAccessible(true);
        ConcurrentHashMap<String, VideoSearchData> searchResults =
                (ConcurrentHashMap<String, VideoSearchData>) searchResultsField.get(cacheManager);
        searchResults.put(searchTerm, cachedData);

        // Act
        WordStatData result = cacheManager.getWordStats(searchTerm);

        // Assert
        assertNotNull(result);
        assertEquals(searchTerm, result.getSearch());
        assertEquals(wordCount, result.getWordCount());
        verify(service, never()).getWordStats(anyString());
    }

    /**
     * Tests the case when the service throws an exception while fetching word statistics.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test(expected = IOException.class)
    public void testGetWordStats_ServiceThrowsException() throws Exception {
        String searchTerm = "exceptionQuery";
        when(service.getWordStats(searchTerm)).thenThrow(new IOException("Test Exception"));

        cacheManager.getWordStats(searchTerm);

        verify(service).getWordStats(searchTerm);
    }

    /**
     * Tests the cache hit scenario for fetching video tags.
     * Verifies that the video tags are retrieved from the cache and not from the service.
     *
     * @throws IOException if an I/O exception occurs during the test
     * @throws NoSuchFieldException if the field is not found
     * @throws IllegalAccessException if access to the field is restricted
     */
    @Test
    public void testGetVideoTags_CacheHit() throws IOException, NoSuchFieldException, IllegalAccessException {
        String videoId = "video123";
        List<String> mockTags = List.of("tag1", "tag2", "tag3");

        Field field = CacheManager.class.getDeclaredField("videoTagsCache");
        field.setAccessible(true);
        Map<String, List<String>> videoTagsCache = (Map<String, List<String>>) field.get(cacheManager);
        videoTagsCache.put(videoId, mockTags);

        List<String> result = cacheManager.getVideoTags(videoId);

        assertNotNull(result);
        assertEquals(mockTags, result);
    }
}
