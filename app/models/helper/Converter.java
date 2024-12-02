package models.helper;

import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.SearchResult;
import models.data.ChannelMetaData;
import models.data.ChannelVideoData;
import models.data.Constants;
import models.data.VideoData;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class contains utility methods for converting data from YouTube API objects to custom data model objects.
 * It provides methods for converting search results to video data and channel video data.
 *
 */
public class Converter {

    /**
     * Converts a list of YouTube search results to a list of {@link VideoData} objects.
     *
     * @param results the list of YouTube search results
     * @return a list of {@link VideoData} objects representing the YouTube videos
     */
    public static List<VideoData> convertToVideoData(List<SearchResult> results) {
        return results.stream()
                .map(result -> new VideoData(
                        result.getId().getVideoId(),
                        result.getSnippet().getTitle(),
                        result.getSnippet().getChannelTitle(),
                        result.getSnippet().getChannelId(),
                        result.getSnippet().getThumbnails().getDefault().getUrl(),
                        result.getSnippet().getDescription()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Converts a list of YouTube search results to a {@link ChannelVideoData} object,
     * including channel information from the YouTube API.
     *
     * @param results the list of YouTube search results
     * @param channelId the ID of the channel
     * @param channelInfo the channel's information retrieved from the YouTube API
     * @return a {@link ChannelVideoData} object containing the video and channel information
     */
    public static ChannelVideoData convertToChannelVideoData(List<SearchResult> results, String channelId, Channel channelInfo) {
        List<VideoData> videos = results.stream()
                .map(result -> new VideoData(
                        result.getId().getVideoId(),
                        result.getSnippet().getTitle(),
                        channelInfo.getSnippet().getTitle(),
                        channelId,
                        result.getSnippet().getThumbnails().getDefault().getUrl(),
                        result.getSnippet().getDescription()
                ))
                .collect(Collectors.toList());
        ChannelMetaData channelMetaData = new ChannelMetaData();
        channelMetaData.setTitle(channelInfo.getSnippet().getTitle());
        channelMetaData.setDescription(channelInfo.getSnippet().getDescription());
        channelMetaData.setSubscriberCount(channelInfo.getStatistics().getSubscriberCount());
        channelMetaData.setViewCount(channelInfo.getStatistics().getViewCount());
        channelMetaData.setVideoCount(channelInfo.getStatistics().getVideoCount());
        channelMetaData.setVideoIds(videos.stream().map(VideoData::getVideoId).collect(Collectors.toList()));
        return new ChannelVideoData(videos, channelMetaData);
    }

    /**
     * Updates the search session string by appending the new query and ensuring uniqueness.
     * It limits the number of entries to a maximum defined by {@link Constants#MAX_VIDEOS_DISPLAY_COUNT}.
     *
     * @param query the new search query
     * @param currentSession the current search session string
     * @return the updated search session string
     */
    public static String getUpdatedSearchSession(String query, String currentSession) {
        currentSession = query + "," + currentSession;

        return Arrays.stream(currentSession.split(","))
                .filter(e -> !e.isEmpty())
                .map(k -> k.toLowerCase().trim())
                .distinct()
                .limit(Constants.MAX_VIDEOS_DISPLAY_COUNT)
                .collect(Collectors.joining(","));
    }
}
