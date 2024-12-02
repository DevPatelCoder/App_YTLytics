package models.api;

import models.data.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CacheManager class to manage caching of video search results, channel metadata, and video tags.
 */
@Singleton
public class CacheManager {

    private static ConcurrentHashMap<String, VideoSearchData> searchResults;
    private static ConcurrentHashMap<String, ChannelVideoData> channelMetaDataHashMap;
    private static ConcurrentHashMap<String, List<String>> videoTagsCache; // Cache for video tags
    private YouTubeService service;

    /**
     * Creates a new YouTubeService instance with the given API key.
     *
     * @param apiKey The API key for accessing YouTube service.
     * @return A new instance of YouTubeService.
     * @throws GeneralSecurityException If there is a security issue.
     * @throws IOException If an IO exception occurs.
     */
    protected YouTubeService createYouTubeService(String apiKey) throws GeneralSecurityException, IOException {
        return new YouTubeService(apiKey);
    }

    /**
     * Constructor to initialize CacheManager and YouTubeService with API key.
     *
     * @param config The configuration containing the API key.
     * @throws GeneralSecurityException If there is a security issue.
     * @throws IOException If an IO exception occurs.
     */
    @Inject
    protected CacheManager(com.typesafe.config.Config config) throws GeneralSecurityException, IOException {
        String apiKey = config.getString("youtube.api.key");
        this.service = createYouTubeService(apiKey);
        this.searchResults = new ConcurrentHashMap<>();
        this.channelMetaDataHashMap = new ConcurrentHashMap<>();
        this.videoTagsCache = new ConcurrentHashMap<>(); // Initialize video tags cache
    }

    /**
     * Retrieves word statistics for a given search term.
     *
     * @param searchTerm The search term for which to get word statistics.
     * @return The word statistics data for the search term.
     * @throws IOException If an IO exception occurs.
     */
    public WordStatData getWordStats(String searchTerm) throws IOException {
        VideoSearchData existing = searchResults.getOrDefault(searchTerm, null);
        if (Objects.isNull(existing) || Objects.isNull(existing.getWordCount()) || existing.getWordCount().isEmpty()) {
            WordStatData data = service.getWordStats(searchTerm);
            searchResults.get(searchTerm).setWordCount(data.getWordCount());
        }

        return new WordStatData(searchTerm, searchResults.get(searchTerm).getWordCount());
    }

    /**
     * Retrieves channel results for a given channel ID.
     *
     * @param channel The channel ID to retrieve results for.
     * @return The channel metadata associated with the channel ID.
     * @throws IOException If an IO exception occurs.
     */
    public ChannelVideoData getChannelResults(String channel) throws IOException {
      if (!channelMetaDataHashMap.containsKey(channel)) {
         addToChannelCache(channel);
       }
       return channelMetaDataHashMap.get(channel); // Return null if not found
   }

    /**
     * Retrieves tags associated with a video by its video ID.
     *
     * @param videoId The video ID for which to retrieve the tags.
     * @return A list of tags associated with the video.
     * @throws IOException If an IO exception occurs.
     */
    public List<String> getVideoTags(String videoId) throws IOException {

        if (!videoTagsCache.containsKey(videoId)) {
            addTagsToCache(videoId); // Fetch and cache tags if not already cached
        }

        for (Map.Entry<String, List<String>> entry : videoTagsCache.entrySet()) {
            if (entry.getKey().equals(videoId)) {
                return entry.getValue();
            }
        }

        // In case something unexpected happens (fallback)
        return new ArrayList<>(); // Or throw an exception if needed
    }

    /**
     * Adds channel metadata to the cache for a given channel ID.
     *
     * @param channelId The channel ID to add to the cache.
     * @throws IOException If an IO exception occurs.
     */
    private void addToChannelCache(String channelId) throws IOException {
      ChannelVideoData data = service.getChannelRecentVideos(channelId, Constants.MAX_VIDEOS_DISPLAY_COUNT);

      if (data != null) {
          channelMetaDataHashMap.put(channelId, data);
       }
       // If data is null, do not insert anything into the cache
   }

    /**
     * Adds video tags to the cache for a given video ID.
     *
     * @param videoId The video ID to add tags for.
     * @throws IOException If an IO exception occurs.
     */
    private void addTagsToCache(String videoId) throws IOException {
        List<String> tags = service.getVideoTags(videoId); // Fetch tags from YouTubeService
        videoTagsCache.put(videoId, tags); // Cache the tags
    }

}
