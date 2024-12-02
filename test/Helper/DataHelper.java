package Helper;

import com.google.api.services.youtube.model.*;
import controllers.HomeControllerTest;
import models.data.Sentiment;
import models.data.VideoData;
import models.data.VideoSearchData;
import play.mvc.Http;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataHelper {

    public static VideoSearchData getSearchData(String query){
        List<VideoData> videoDataList = getVideoData();
        return new VideoSearchData(query, videoDataList, Sentiment.NEUTRAL, 0.0, 0.0, null);
    }

    public static List<VideoData> getVideoData(){
        return IntStream.range(0, 10)
                .mapToObj(i -> new VideoData(
                        "videoId" + i,
                        "title" + i,
                        "channelTitle" + i,
                        "channelId" + i,
                        "thumbnailUrl" + i,
                        "description" + i
                ))
                .collect(Collectors.toList());
    }

    public static Http.Request getHttpRequest(String search){
        // Create a mock HTTP request with session data
        Map<String, String> sessionData = new HashMap<>();
        sessionData.put(HomeControllerTest.SESSION_KEY, search);

        // Build the request with the mock session
        return new Http.RequestBuilder()
                .session(sessionData)
                .build();
    }

    // Improved mock response for YouTube search list
    public static SearchListResponse getYouTubeSearchListResponse(String videoId, String title, String desc,
                                                                  String channelTitle, String channelId, String url) {
        SearchListResponse response = new SearchListResponse();
        List<SearchResult> items = new ArrayList<>();

        // Create a mock search result
        SearchResult searchResult = new SearchResult();

        // Set up ResourceId (video ID)
        ResourceId resourceId = new ResourceId();
        resourceId.setVideoId(videoId);
        searchResult.setId(resourceId);

        // Set up Snippet
        SearchResultSnippet snippet = new SearchResultSnippet();
        snippet.setTitle(title);
        snippet.setDescription(desc);
        snippet.setChannelTitle(channelTitle);
        snippet.setChannelId(channelId);

        // Set up Thumbnail
        ThumbnailDetails thumbnailDetails = new ThumbnailDetails();
        Thumbnail thumbnail = new Thumbnail();
        thumbnail.setUrl(url);
        thumbnailDetails.setDefault(thumbnail);
        snippet.setThumbnails(thumbnailDetails);

        searchResult.setSnippet(snippet);
        items.add(searchResult);
        response.setItems(items);
        return response;
    }
    // Improved mock Channel setup
    public static Channel getMockChannel(String channelId, String title, String subscriberCount) {
        Channel channel = new Channel();
        channel.setId(channelId);

        // Setting up snippet with the title of the channel
        ChannelSnippet snippet = new ChannelSnippet();
        snippet.setTitle(title);
        snippet.setDescription("Test Channel Description");  // Added description here
        channel.setSnippet(snippet);

        // Setting up statistics for the channel
        ChannelStatistics statistics = new ChannelStatistics();
        BigInteger subscriberCountBigInt = new BigInteger(subscriberCount);
        statistics.setSubscriberCount(subscriberCountBigInt);
        channel.setStatistics(statistics);

        return channel;
    }


    // Updated method to return video data with channel and video details
    public static VideoData getMockVideoData(String videoId, String title, String description,
                                             String channelTitle, String channelId, String thumbnailUrl) {
        return new VideoData(
                videoId, title, channelTitle, channelId, thumbnailUrl, description
        );
    }

    public static List<VideoData> getRecentVideos() {
        return IntStream.range(0, 5) // Adjust the number as needed for your test
                .mapToObj(i -> getMockVideoData(
                        "videoId" + i,
                        "title" + i,
                        "description" + " for channel",
                        "channelTitle" + i,
                        "someChannelId",  // You can just set a placeholder channelId here if needed
                        "thumbnailUrl" + i
                ))
                .collect(Collectors.toList());
    }


    public static VideoListResponse getYouTubeVideoListResponse(String videoId, String description) {
        // Create a mock video item
        Video video = new Video();
        VideoSnippet snippet = new VideoSnippet();
        snippet.setDescription(description);
        video.setSnippet(snippet);
        video.setId(videoId);

        // Create the response and add the video item
        VideoListResponse response = new VideoListResponse();
        response.setItems(List.of(video));
        return response;
    }
}