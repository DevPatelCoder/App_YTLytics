package models.api;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import models.data.*;
import models.helper.Converter;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for interacting with the YouTube Data API.
 * <p>
 * This service allows searching for videos, retrieving video and channel details, fetching video tags,
 * and performing sentiment analysis on video descriptions. It also supports analyzing word statistics
 * from video descriptions based on a search term.
 * </p>
 */
public class YouTubeService {

    private static final String APPLICATION_NAME = "TubeLytics";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final YouTube youtube;
    private final String apiKey;

    /**
     * Creates a YouTube client using the provided API credentials.
     *
     * @return a configured {@link YouTube} client
     * @throws GeneralSecurityException if there is an error related to security
     * @throws IOException if there is an error during the creation of the client
     *
     */
    protected YouTube createYouTubeClient() throws GeneralSecurityException, IOException {
        return new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY,
                null).setApplicationName(APPLICATION_NAME).build();
    }

    /**
     * Constructor for initializing YouTubeService with the API key.
     *
     * @param apiKey the YouTube Data API key
     * @throws GeneralSecurityException if there is a security error
     * @throws IOException if there is an I/O error during the initialization
     *
     */
    public YouTubeService(String apiKey) throws GeneralSecurityException, IOException {
        this.apiKey = apiKey;
        this.youtube = createYouTubeClient();
    }

    /**
     * Searches for videos based on the specified query term and retrieves up to a maximum number of results.
     *
     * @param queryTerm the search term to query YouTube for
     * @param maxCount the maximum number of search results to return
     * @return a list of {@link VideoData} objects corresponding to the search results
     * @throws IOException if there is an error during the search
     *
     */
    public List<VideoData> searchVideos(String queryTerm, int maxCount) throws IOException {
        YouTube.Search.List search = youtube.search().list(List.of("id", "snippet"));
        search.setQ(queryTerm);
        search.setType(List.of("video"));
        search.setMaxResults(Long.valueOf(maxCount));
        search.setOrder("date"); // Sort by upload date, newest first

        // Fields to retrieve relevant information including channelId
        search.setFields("items(id/videoId,snippet/title,snippet/thumbnails/default/url,snippet/channelTitle,snippet/channelId,snippet/description)");
        search.setKey(apiKey);
        SearchListResponse response = search.execute();
        // call converter method to convert search results to VideoData list
        return Converter.convertToVideoData(response.getItems());
    }

    /**
     * Retrieves the details of a channel by its channel ID.
     *
     * @param channelId the ID of the channel whose details are to be fetched
     * @return a {@link Channel} object containing the channel details, or null if not found
     * @throws IOException if there is an error while fetching channel details
     *
     */
    public Channel getChannelDetails(String channelId) throws IOException {
        YouTube.Channels.List request = youtube.channels().list(List.of("snippet", "statistics"));
        request.setId(List.of(channelId));
        request.setFields("items(id,snippet/title,snippet/description,statistics/subscriberCount,statistics/viewCount,statistics/videoCount)");
        request.setKey(apiKey);

        ChannelListResponse response = request.execute();
        return response.getItems().isEmpty() ? null : response.getItems().get(0);
    }

    /**
     * Retrieves the last set of videos from a specified channel.
     *
     * @param channelId the ID of the channel whose recent videos are to be fetched
     * @param maxResults the maximum number of videos to retrieve
     * @return a {@link ChannelVideoData} object containing details of the channel and recent videos
     * @throws IOException if there is an error during the fetch
     *
     */
    public ChannelVideoData getChannelRecentVideos(String channelId, int maxResults) throws IOException {

        Channel channelInfo = getChannelDetails(channelId);

        YouTube.Search.List search = youtube.search().list(List.of("id", "snippet"));
        search.setChannelId(channelId);
        search.setOrder("date");
        search.setMaxResults((long) maxResults);
        search.setType(List.of("video"));

        // Fields to retrieve necessary information for each video
        search.setFields("items(id/videoId,snippet/title,snippet/thumbnails/default/url,snippet/description)");
        search.setKey(apiKey);

        SearchListResponse response = search.execute();
        List<SearchResult> results =  response.getItems() != null ? response.getItems() : List.of();
        return Converter.convertToChannelVideoData(results, channelId, channelInfo);
    }

    /**
     * Retrieves the tags associated with a specific video by its video ID.
     *
     * @param videoId the ID of the video whose tags are to be fetched
     * @return a list of tags associated with the video, or an empty list if no tags exist
     * @throws IOException if there is an error during the fetch
     *
     */
    public List<String> getVideoTags(String videoId) throws IOException {
        YouTube.Videos.List request = youtube.videos().list(List.of("snippet"));
        request.setId(Collections.singletonList(videoId));
        request.setKey(apiKey);

        VideoListResponse response = request.execute();
        if (response.getItems() != null && !response.getItems().isEmpty()) {
            Video video = response.getItems().get(0);
            // Check for snippet and tags to avoid NullPointerException
            return video.getSnippet() != null && video.getSnippet().getTags() != null
                    ? video.getSnippet().getTags()
                    : List.of();
        }
        return List.of();
    }


    /**
     * Analyzes the sentiment of the full descriptions of videos based on a search term.
     *
     * @param queryTerm the search term to query YouTube for
     * @param maxResults the maximum number of results to analyze
     * @return a {@link Sentiment} object containing sentiment analysis results
     * @throws IOException if there is an error during the sentiment analysis
     *
     */
    public Sentiment getSentimentalAnalysis(String queryTerm, int maxResults) throws IOException {

        List<Video> videos = getVideosFromSearchTermWithFullDescription(queryTerm, maxResults);

        // Process full descriptions
        return AnalyseSentiment.analyzeStreamSentiment(
                videos.stream()
                        .parallel()
                        .map(video -> video.getSnippet().getDescription())
                        .collect(Collectors.toList())
        );
    }

    /**
     * Calculates the average Flesch-Kincaid Grade Level for video descriptions based on a query term.
     *
     * @param queryTerm The search query term.
     * @param maxResults The maximum number of results to include in the Flesch-Kincaid analysis.
     * @return The average Flesch-Kincaid Grade Level score for the video descriptions.
     * @throws IOException If there is an issue with the YouTube API request.
     */
    public double getavgFleshGrade(String queryTerm, int maxResults) throws IOException {

        List<Video> videos = getVideosFromSearchTermWithFullDescription(queryTerm, maxResults);

        // Process full descriptions
        return ReadabilityGrade.calculateAverageFleschKincaid(
                videos.stream()
                        .parallel()
                        .map(video -> video.getSnippet().getDescription())
                        .collect(Collectors.toList())
        );
    }
    /**
     * Calculates the average Flesch Reading Ease score for video descriptions based on a query term.
     *
     * @param queryTerm The search query term.
     * @param maxResults The maximum number of results to include in the Flesch Reading Ease analysis.
     * @return The average Flesch Reading Ease score for the video descriptions.
     * @throws IOException If there is an issue with the YouTube API request.
     */
    public double getavgFleshScore(String queryTerm, int maxResults) throws IOException {

        List<Video> videos = getVideosFromSearchTermWithFullDescription(queryTerm, maxResults);

        // Process full descriptions
        return ReadabilityGrade.calculateAverageFleschReadingEase(
                videos.stream()
                        .parallel()
                        .map(video -> video.getSnippet().getDescription())
                        .collect(Collectors.toList())
        );
    }


    public double getFleshGrade(String queryTerm, int maxResults) throws IOException {

        List<Video> videos = getVideosFromSearchTermWithFullDescription(queryTerm, maxResults);

        // Process full descriptions
        return ReadabilityGrade.calculateFleschKincaidGradeLevel(
                String.valueOf(videos.stream()
                        .parallel()
                        .map(video -> video.getSnippet().getDescription())
                        .collect(Collectors.toList()))
        );
    }
    public double getFleshScore(String queryTerm, int maxResults) throws IOException {

        List<Video> videos = getVideosFromSearchTermWithFullDescription(queryTerm, maxResults);

        // Process full descriptions
        return ReadabilityGrade.calculateFleschReadingEase(
                String.valueOf(videos.stream()
                        .parallel()
                        .map(video -> video.getSnippet().getDescription())
                        .collect(Collectors.toList()))
        );
    }


    /**
     * Analyzes word statistics based on the full descriptions of videos retrieved by a search term.
     *
     * @param queryTerm the search term to query YouTube for
     * @return a {@link WordStatData} object containing word frequency statistics
     * @throws IOException if there is an error during the analysis
     *
     */
    public WordStatData getWordStats(String queryTerm) throws IOException {
        List<Video> videos = getVideosFromSearchTermWithFullDescription(queryTerm, Constants.MAX_DESC_SENTIMENT_COUNT);
        Map<String,Integer> map =  AnalyseWords.analyzeWordStats(
                videos.stream()
                        .parallel()
                        .map(video -> video.getSnippet().getDescription())
                        .collect(Collectors.toList()));
        return new WordStatData(queryTerm, map);
    }

    /**
     * Fetches the full description of videos based on a search term.
     *
     * @param searchTerm the search term to query YouTube for
     * @param maxResults the maximum number of videos to retrieve
     * @return a list of {@link Video} objects corresponding to the search results
     * @throws IOException if there is an error during the video retrieval
     *
     */
    private List<Video> getVideosFromSearchTermWithFullDescription(String searchTerm, int maxResults) throws IOException {
        YouTube.Search.List search = youtube.search().list(List.of("id", "snippet"));
        search.setQ(searchTerm);
        search.setType(List.of("video"));
        search.setMaxResults(Long.valueOf(maxResults));
        search.setOrder("date"); // Sort by upload date, newest first
        // Fields to retrieve relevant information including channelId
        search.setFields("items(id/videoId,snippet/title,snippet/thumbnails/default/url,snippet/channelTitle,snippet/channelId,snippet/description)");
        search.setKey(apiKey);
        SearchListResponse searchResponse = search.execute();
        List<SearchResult> searchResults = searchResponse.getItems() != null ? searchResponse.getItems() : List.of();
        List<String> videoIds = searchResults.stream().map(result -> result.getId().getVideoId()).collect(Collectors.toList());
        YouTube.Videos.List videoRequest = youtube.videos().list(List.of("snippet"));
        videoRequest.setId(videoIds);
        videoRequest.setFields("items(snippet/description)");
        videoRequest.setKey(apiKey);

        VideoListResponse videoResponse = videoRequest.execute();
        return videoResponse.getItems() != null ? videoResponse.getItems() : List.of();
    }

}
