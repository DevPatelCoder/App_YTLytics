package models.api;

import Helper.DataHelper;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import models.data.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;
/**
 * Unit tests for the {@link YouTubeService} class.
 * These tests cover the functionality of interacting with the YouTube API for searching videos,
 * analyzing sentiment, and fetching recent channel videos.
 *
 * @author Anubhav Mahajan
 * @author Arpnik Singh
 * @author Shashwat Janakkumar Shah
 */
public class YouTubeServiceTest {

    public static YouTubeService service;
    private static final String API_KEY = "test-api-key";
    private static final String QUERY = "test query";
    private static final int MAX_RESULTS = 5;

    @Mock
    private YouTube.Videos videosMock;
    @Mock
    private YouTube youtube;

    @Mock
    private YouTube.Search youtubeSearch;

    @Mock
    private YouTube.Search.List searchList;

    @Mock
    private YouTube.Channels youtubeChannels;

    @Mock
    private YouTube.Videos.List videosList;

    @Mock
    private YouTube.Videos youtubeVideos;

    private YouTube.Videos.List videoListRequest;

    @Mock
    private YouTube.Channels.List channelsList;
    /**
     * Setup method to initialize mocks before each test.
     *
     * @throws GeneralSecurityException if there is an error with security settings.
     * @throws IOException if there is an I/O error while initializing mocks.
     * @author Anubhav Mahajan
     * @author Arpnik Singh
     */

    @Before
    public void setUp() throws GeneralSecurityException, IOException {
        MockitoAnnotations.openMocks(this);


        // Setup YouTube videos mocks
        when(youtube.videos()).thenReturn(youtubeVideos);
        when(youtubeVideos.list(anyList())).thenReturn(videosList);


        // Mock the YouTube search and channel methods
        when(youtube.search()).thenReturn(youtubeSearch); // youtubeSearch is of type YouTube.Search
        when(youtube.channels()).thenReturn(youtubeChannels);

        // Set up mock for YouTube.Search.List
        when(youtubeSearch.list(anyList())).thenReturn(searchList);

        // Mock YouTube.Videos to return the videosMock
        when(youtube.videos()).thenReturn(videosMock);
        when(videosMock.list(anyList())).thenReturn(videosList);


        // Mock the YouTube.Videos class and its methods
        YouTube.Videos youtubeVideos = mock(YouTube.Videos.class);
        when(youtube.videos()).thenReturn(youtubeVideos);

        // Mock the YouTube.Videos.List object
        videoListRequest = mock(YouTube.Videos.List.class);
        when(youtubeVideos.list(anyList())).thenReturn(videoListRequest);

        when(youtubeChannels.list(anyList())).thenReturn(channelsList);
        when(channelsList.setId(anyList())).thenReturn(channelsList);
        when(channelsList.setFields(anyString())).thenReturn(channelsList);
        when(channelsList.setKey(anyString())).thenReturn(channelsList);

        // Initialize the service with the mocked YouTube client
        service = new YouTubeService("dummy-api") {
            @Override
            protected YouTube createYouTubeClient() {
                return youtube;
            }
        };
    }
    /**
     * Test for searching videos and fetching data from YouTube API.
     * Verifies that the YouTube API is called correctly and the data is returned as expected.
     *
     * @throws Exception if any error occurs during the test.
     *  author Anubhav Mahajan
     *  @author Arpnik Singh
     */
    @Test
    public void searchVideosAndFetchFromYoutubeApi() throws Exception {
        // Prepare mock response for search videos
        SearchListResponse mockResponse = DataHelper.getYouTubeSearchListResponse("test-video-id",
                "Test Video Title", "Test Description", "Test Channel", "test-channel-id", "http://test-thumbnail.jpg");

        // Set up mock behavior
        when(searchList.setQ(QUERY)).thenReturn(searchList);
        when(searchList.setType(anyList())).thenReturn(searchList);
        when(searchList.setMaxResults(Long.valueOf(MAX_RESULTS))).thenReturn(searchList);
        when(searchList.setFields(anyString())).thenReturn(searchList);
        when(searchList.setKey(API_KEY)).thenReturn(searchList);
        when(searchList.execute()).thenReturn(mockResponse);

        // Execute the method
        List<VideoData> result = service.searchVideos(QUERY, MAX_RESULTS);

        // Verify the results
        assertNotNull(result);
        assertEquals(1, result.size());
        VideoData videoData = result.get(0);
        assertEquals("test-video-id", videoData.getVideoId());
        assertEquals("Test Video Title", videoData.getTitle());
        assertEquals("Test Channel", videoData.getChannelTitle());
        assertEquals("test-channel-id", videoData.getChannelId());
        assertEquals("http://test-thumbnail.jpg", videoData.getThumbnailUrl());
        assertEquals("Test Description", videoData.getDescription());

        // Verify that all expected methods were called
        verify(searchList).setQ(QUERY);
        verify(searchList).setType(List.of("video"));
        verify(searchList).setMaxResults(Long.valueOf(MAX_RESULTS));
        verify(searchList).setFields(contains("items(id/videoId,snippet/title"));
        verify(searchList).execute();
    }
    /**
     * Test for sentiment analysis based on video descriptions.
     * Verifies that the sentiment is analyzed correctly as HAPPY when appropriate.
     *
     * @throws IOException if there is an I/O error while fetching video data.
     * @author Arpnik Singh
     */
    @Test
    public void testSentimentAnalysis() throws IOException {
        VideoListResponse mockVideoResponse = new VideoListResponse();

        // Mock the video response (Video data)
        Video video = new Video();
        VideoSnippet snippet = new VideoSnippet();
        snippet.setDescription("I am happy and excited.");
        video.setSnippet(snippet);

        // Set the mock response with the video data
        mockVideoResponse.setItems(List.of(video));

        // When execute() is called, return the mock response
        when(videoListRequest.execute()).thenReturn(mockVideoResponse);

        String search = "channel";
        // Create mock search result
        SearchResult searchResult = new SearchResult();
        ResourceId resourceId = new ResourceId();
        resourceId.setVideoId("test-video-id");
        searchResult.setId(resourceId);

        // Create mock search response
        SearchListResponse searchListResponse = new SearchListResponse();
        searchListResponse.setItems(Arrays.asList(searchResult));

        // Create mock video
        Video mockVideo = new Video();
        mockVideo.setSnippet(snippet);

        // Create mock video response
        VideoListResponse videoListResponse = new VideoListResponse();
        videoListResponse.setItems(Arrays.asList(mockVideo));

        // Setup search list mocks
        when(searchList.setQ(search)).thenReturn(searchList);
        when(searchList.setType(anyList())).thenReturn(searchList);
        when(searchList.setMaxResults(Long.valueOf(MAX_RESULTS))).thenReturn(searchList);
        when(searchList.setFields(anyString())).thenReturn(searchList);
        when(searchList.setKey(API_KEY)).thenReturn(searchList);
        when(searchList.setOrder(anyString())).thenReturn(searchList);
        when(searchList.execute()).thenReturn(searchListResponse);

        // Setup video list mocks
        when(videosList.setId(anyList())).thenReturn(videosList);
        when(videosList.setFields(anyString())).thenReturn(videosList);
        when(videosList.setKey(API_KEY)).thenReturn(videosList);
        when(videosList.execute()).thenReturn(videoListResponse);

        // Execute the test
        Sentiment sentiment = service.getSentimentalAnalysis(search, Constants.MAX_DESC_SENTIMENT_COUNT);
        assertEquals(Sentiment.HAPPY, sentiment);
    }
    /**
     * Test for sentiment analysis with sad sentiment.
     * Verifies that the sentiment is analyzed correctly as SAD when appropriate.
     *
     * @throws IOException if there is an I/O error while fetching video data.
     * @author Arpnik Singh
     */


    @Test
    public void testSentimentAnalysisWithSad() throws IOException {

        // Mock the execute() method to return a VideoListResponse
        VideoListResponse mockVideoResponse = new VideoListResponse();

        // Mock the video response (Video data)
        Video video = new Video();
        VideoSnippet snippet = new VideoSnippet();
        snippet.setDescription("I am sad and depressed after watching this video.");
        video.setSnippet(snippet);

        // Set the mock response with the video data
        mockVideoResponse.setItems(List.of(video));

        // When execute() is called, return the mock response
        when(videoListRequest.execute()).thenReturn(mockVideoResponse);

        String search = "channel";
        // Create mock search result
        SearchResult searchResult = new SearchResult();
        ResourceId resourceId = new ResourceId();
        resourceId.setVideoId("test-video-id");
        searchResult.setId(resourceId);

        // Create mock search response
        SearchListResponse searchListResponse = new SearchListResponse();
        searchListResponse.setItems(Arrays.asList(searchResult));

        // Create mock video
        Video mockVideo = new Video();
        mockVideo.setSnippet(snippet);

        // Create mock video response
        VideoListResponse videoListResponse = new VideoListResponse();
        videoListResponse.setItems(Arrays.asList(mockVideo));

        // Setup search list mocks
        when(searchList.setQ(search)).thenReturn(searchList);
        when(searchList.setType(anyList())).thenReturn(searchList);
        when(searchList.setMaxResults(Long.valueOf(MAX_RESULTS))).thenReturn(searchList);
        when(searchList.setFields(anyString())).thenReturn(searchList);
        when(searchList.setKey(API_KEY)).thenReturn(searchList);
        when(searchList.setOrder(anyString())).thenReturn(searchList);
        when(searchList.execute()).thenReturn(searchListResponse);

        // Setup video list mocks
        when(videosList.setId(anyList())).thenReturn(videosList);
        when(videosList.setFields(anyString())).thenReturn(videosList);
        when(videosList.setKey(API_KEY)).thenReturn(videosList);
        when(videosList.execute()).thenReturn(videoListResponse);

        // Execute the test
        Sentiment sentiment = service.getSentimentalAnalysis(search, Constants.MAX_DESC_SENTIMENT_COUNT);
        assertEquals(Sentiment.SAD, sentiment);
    }
    /**
     * Test for sentiment analysis with neutral sentiment.
     * Verifies that the sentiment is analyzed correctly as NEUTRAL when appropriate.
     *
     * @throws IOException if there is an I/O error while fetching video data.
     * @author Arpnik Singh
     */
    @Test
    public void testSentimentAnalysisWithNeutral() throws IOException {

        // Mock the execute() method to return a VideoListResponse
        VideoListResponse mockVideoResponse = new VideoListResponse();

        // Mock the video response (Video data)
        Video video = new Video();
        VideoSnippet snippet = new VideoSnippet();
        snippet.setDescription("Table, chair, city, and dustbin");
        video.setSnippet(snippet);

        // Set the mock response with the video data
        mockVideoResponse.setItems(List.of(video));

        // When execute() is called, return the mock response
        when(videoListRequest.execute()).thenReturn(mockVideoResponse);

        String search = "channel";
        // Create mock search result
        SearchResult searchResult = new SearchResult();
        ResourceId resourceId = new ResourceId();
        resourceId.setVideoId("test-video-id");
        searchResult.setId(resourceId);

        // Create mock search response
        SearchListResponse searchListResponse = new SearchListResponse();
        searchListResponse.setItems(Arrays.asList(searchResult));

        // Create mock video
        Video mockVideo = new Video();
        mockVideo.setSnippet(snippet);

        // Create mock video response
        VideoListResponse videoListResponse = new VideoListResponse();
        videoListResponse.setItems(Arrays.asList(mockVideo));

        // Setup search list mocks
        when(searchList.setQ(search)).thenReturn(searchList);
        when(searchList.setType(anyList())).thenReturn(searchList);
        when(searchList.setMaxResults(Long.valueOf(MAX_RESULTS))).thenReturn(searchList);
        when(searchList.setFields(anyString())).thenReturn(searchList);
        when(searchList.setKey(API_KEY)).thenReturn(searchList);
        when(searchList.setOrder(anyString())).thenReturn(searchList);
        when(searchList.execute()).thenReturn(searchListResponse);

        // Setup video list mocks
        when(videosList.setId(anyList())).thenReturn(videosList);
        when(videosList.setFields(anyString())).thenReturn(videosList);
        when(videosList.setKey(API_KEY)).thenReturn(videosList);
        when(videosList.execute()).thenReturn(videoListResponse);

        // Execute the test
        Sentiment sentiment = service.getSentimentalAnalysis(search, Constants.MAX_DESC_SENTIMENT_COUNT);
        assertEquals(Sentiment.NEUTRAL, sentiment);
    }
    /**
     * Test for fetching recent videos from a YouTube channel.
     * Verifies that recent videos are fetched correctly based on the provided channel ID.
     *
     * @throws IOException if there is an I/O error while fetching video data.
     * @author Anubhav Mahajan
     */
    @Test
    public void testGetChannelRecentVideos2() throws IOException {
        String channelId = "test-channel-id";
        int maxResults = 10;

        // Mock channel details response
        Channel mockChannel = new Channel();
        ChannelSnippet channelSnippet = new ChannelSnippet();
        channelSnippet.setTitle("Test Channel");
        channelSnippet.setDescription("Test Description");
        mockChannel.setSnippet(channelSnippet);

        ChannelStatistics statistics = new ChannelStatistics();
        statistics.setSubscriberCount(new BigInteger(String.valueOf(1000L)));
        statistics.setViewCount(new BigInteger(String.valueOf(5000)));
        statistics.setVideoCount(new BigInteger(String.valueOf(100)));
        mockChannel.setStatistics(statistics);

        ChannelListResponse channelListResponse = new ChannelListResponse();
        channelListResponse.setItems(List.of(mockChannel));

        // Mock search results
        SearchResult searchResult = new SearchResult();
        ResourceId resourceId = new ResourceId();
        resourceId.setVideoId("test-video-id");
        searchResult.setId(resourceId);

        SearchResultSnippet searchSnippet = new SearchResultSnippet();
        searchSnippet.setTitle("Test Video Title");
        searchSnippet.setDescription("Test Video Description");

        ThumbnailDetails thumbnails = new ThumbnailDetails();
        Thumbnail defaultThumbnail = new Thumbnail();
        defaultThumbnail.setUrl("http://test-thumbnail.jpg");
        thumbnails.setDefault(defaultThumbnail);

        searchSnippet.setThumbnails(thumbnails);
        searchResult.setSnippet(searchSnippet);

        SearchListResponse searchListResponse = new SearchListResponse();
        searchListResponse.setItems(List.of(searchResult));

        // Setup channel list mocks
        when(channelsList.execute()).thenReturn(channelListResponse);

        // Setup search list mocks
        when(searchList.setChannelId(channelId)).thenReturn(searchList);
        when(searchList.setOrder("date")).thenReturn(searchList);
        when(searchList.setMaxResults((long) maxResults)).thenReturn(searchList);
        when(searchList.setType(anyList())).thenReturn(searchList);
        when(searchList.setFields(contains("items(id/videoId"))).thenReturn(searchList);
        when(searchList.setKey(anyString())).thenReturn(searchList);
        when(searchList.execute()).thenReturn(searchListResponse);

        // Execute the test
        ChannelVideoData result = service.getChannelRecentVideos(channelId, maxResults);

        // Verify results
        assertNotNull(result);
        assertEquals("Test Channel", result.getChannelData().getTitle());
        assertEquals(1000L, result.getChannelData().getSubscriberCount().longValue());
        assertEquals(5000L, result.getChannelData().getViewCount().longValue());

    }

    /**
     * Test for checking recent video list
     * @throws IOException if there is an I/O error while fetching video data.
     * @author Arpnik Singh
     */
    @Test
    public void searchVideos_ShouldReturnVideoDataList() throws Exception {
        // Prepare mock response
        SearchListResponse mockResponse = DataHelper.getYouTubeSearchListResponse("test-video-id","Test Video Title", "Test Description", "Test Channel", "test-channel-id", "http://test-thumbnail.jpg");

        // Set up mock behavior
        when(searchList.setQ(QUERY)).thenReturn(searchList);
        when(searchList.setType(anyList())).thenReturn(searchList);
        when(searchList.setMaxResults(Long.valueOf(MAX_RESULTS))).thenReturn(searchList);
        when(searchList.setFields(anyString())).thenReturn(searchList);
        when(searchList.setKey(API_KEY)).thenReturn(searchList);
        when(searchList.execute()).thenReturn(mockResponse);

        // Execute the method
        List<VideoData> result = service.searchVideos(QUERY, MAX_RESULTS);

        // Verify the results
        assertNotNull(result);
        assertEquals(1, result.size());
        VideoData videoData = result.get(0);
        assertEquals("test-video-id", videoData.getVideoId());
        assertEquals("Test Video Title", videoData.getTitle());
        assertEquals("Test Channel", videoData.getChannelTitle());
        assertEquals("test-channel-id", videoData.getChannelId());
        assertEquals("http://test-thumbnail.jpg", videoData.getThumbnailUrl());
        assertEquals("Test Description", videoData.getDescription());

        // Verify that all expected methods were called
        verify(searchList).setQ(QUERY);
        verify(searchList).setType(List.of("video"));
        verify(searchList).setMaxResults(Long.valueOf(MAX_RESULTS));
        verify(searchList).setFields(contains("items(id/videoId,snippet/title"));
        verify(searchList).execute();
    }


    /**
     * Test for checking word stat
     * @throws IOException if there is an I/O error while fetching video data.
     * @author Arpnik Singh
     * @author Shashwat Janakkumar Shah
     */
    @Test
    public void getWordStats_ShouldReturnWordStatData() throws IOException {

        // Mock the execute() method to return a VideoListResponse
        VideoListResponse mockVideoResponse = new VideoListResponse();

        // Mock the video response (Video data)
        Video video = new Video();
        VideoSnippet snippet = new VideoSnippet();
        snippet.setDescription("Table, chair, city, and dustbin");
        video.setSnippet(snippet);

        // Set the mock response with the video data
        mockVideoResponse.setItems(List.of(video));

        // When execute() is called, return the mock response
        when(videoListRequest.execute()).thenReturn(mockVideoResponse);

        String search = "channel";
        // Create mock search result
        SearchResult searchResult = new SearchResult();
        ResourceId resourceId = new ResourceId();
        resourceId.setVideoId("test-video-id");
        searchResult.setId(resourceId);

        // Create mock search response
        SearchListResponse searchListResponse = new SearchListResponse();
        searchListResponse.setItems(Arrays.asList(searchResult));

        // Create mock video
        Video mockVideo = new Video();
        mockVideo.setSnippet(snippet);

        // Create mock video response
        VideoListResponse videoListResponse = new VideoListResponse();
        videoListResponse.setItems(Arrays.asList(mockVideo));

        // Setup search list mocks
        when(searchList.setQ(search)).thenReturn(searchList);
        when(searchList.setType(anyList())).thenReturn(searchList);
        when(searchList.setMaxResults(Long.valueOf(MAX_RESULTS))).thenReturn(searchList);
        when(searchList.setFields(anyString())).thenReturn(searchList);
        when(searchList.setKey(API_KEY)).thenReturn(searchList);
        when(searchList.setOrder(anyString())).thenReturn(searchList);
        when(searchList.execute()).thenReturn(searchListResponse);

        // Setup video list mocks
        when(videosList.setId(anyList())).thenReturn(videosList);
        when(videosList.setFields(anyString())).thenReturn(videosList);
        when(videosList.setKey(API_KEY)).thenReturn(videosList);
        when(videosList.execute()).thenReturn(videoListResponse);

        // Execute the method
        WordStatData result = service.getWordStats(QUERY);

        // Verify the result
        assertNotNull(result);
        assertNotNull(result.getWordCount());
        assertEquals(QUERY, result.getSearch());

    }

}