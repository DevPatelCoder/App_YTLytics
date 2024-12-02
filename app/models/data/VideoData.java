package models.data;

import java.util.List;

/**
 * Represents data associated with a video, including the video ID, title, channel information,
 * thumbnail URL, description, tags, and sentiment.
 * This class also provides methods to generate URLs for the video and channel.
 *
 */
public class VideoData {

    /** The unique identifier for the video. */
    private String videoId;

    /** The title of the video. */
    private String title;

    /** The title of the channel that posted the video. */
    private String channelTitle;

    /** The unique identifier for the channel. */
    private String channelId;

    /** The URL of the video's thumbnail. */
    private String thumbnailUrl;

    /** The description of the video. */
    private String description;

    /** A list of tags associated with the video. */
    private List<String> tags;

    /** The sentiment of the video description. */
    private String sentiment;

    /**
     * Constructs a new VideoData object with the provided details about the video.
     *
     * @param videoId the unique identifier of the video
     * @param title the title of the video
     * @param channelTitle the title of the channel
     * @param channelId the unique identifier for the channel
     * @param thumbnailUrl the URL of the video's thumbnail
     * @param description the description of the video
     */
    public VideoData(String videoId, String title, String channelTitle, String channelId, String thumbnailUrl, String description) {
        this.videoId = videoId;
        this.title = title;
        this.channelTitle = channelTitle;
        this.channelId = channelId; // Assign the channel ID
        this.thumbnailUrl = thumbnailUrl;
        this.description = description;
    }

    public VideoData(String id1, String title, String channelTitle, String thumbnail1) {
    }

    /**
     * Gets the unique identifier of the video.
     *
     * @return the video ID
     */
    public String getVideoId() {
        return videoId;
    }

    /**
     * Gets the title of the video.
     *

     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the title of the channel that posted the video
     */
    public String getChannelTitle() {
        return channelTitle;
    }

    /**
     * Gets the unique identifier of the channel.
     *
     * @return the channel ID
     */
    public String getChannelId() {
        return channelId; // Getter for channel ID
    }

    /**
     * Gets the URL of the video's thumbnail.
     *
     * @return the thumbnail URL
     */
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    /**
     * Gets the description of the video.
     *
     * @return the video description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Generates and returns the URL for the video on YouTube.
     *
     * @return the YouTube video URL
     */
    public String getVideoUrl() {
        return "https://www.youtube.com/watch?v=" + videoId;
    }

    /**
     * Generates and returns the URL for the channel's YouTube page.
     *
     * @return the YouTube channel URL
     */
    public String getChannelUrl() {
        return "https://www.youtube.com/channel/" + channelId; // Properly create the channel URL
    }
}
