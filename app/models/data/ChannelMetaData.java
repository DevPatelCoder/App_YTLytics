package models.data;

import java.math.BigInteger;
import java.util.List;

/**
 * Represents the metadata of a YouTube channel, including information
 * such as the channel title, description, view count, subscriber count,
 * video count, and the list of video IDs associated with the channel
 */
public class ChannelMetaData {

    /** The title of the channel. */
    private String title;

    /** The description of the channel. */
    private String description;

    /** The total number of views the channel has. */
    private BigInteger viewCount;

    /** The total number of subscribers of the channel. */
    private BigInteger subscriberCount;

    /** The total number of videos in the channel. */
    private BigInteger videoCount;

    /** A list of video IDs associated with the channel. */
    private List<String> videoIds;

    /**
     * Gets the title of the channel.
     *
     * @return the title of the channel
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the channel.
     *
     * @param title the new title of the channel
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the description of the channel.
     *
     * @return the description of the channel
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the channel.
     *
     * @param description the new description of the channel
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the total view count of the channel.
     *
     * @return the view count of the channel
     */
    public BigInteger getViewCount() {
        return viewCount;
    }

    /**
     * Sets the total view count of the channel.
     *
     * @param viewCount the new view count of the channel
     */
    public void setViewCount(BigInteger viewCount) {
        this.viewCount = viewCount;
    }

    /**
     * Gets the total subscriber count of the channel.
     *
     * @return the subscriber count of the channel
     */
    public BigInteger getSubscriberCount() {
        return subscriberCount;
    }

    /**
     * Sets the total subscriber count of the channel.
     *
     * @param subscriberCount the new subscriber count of the channel
     */
    public void setSubscriberCount(BigInteger subscriberCount) {
        this.subscriberCount = subscriberCount;
    }

    /**
     * Gets the total video count of the channel.
     *
     * @return the video count of the channel
     */
    public BigInteger getVideoCount() {
        return videoCount;
    }

    /**
     * Sets the total video count of the channel.
     *
     * @param videoCount the new video count of the channel
     */
    public void setVideoCount(BigInteger videoCount) {
        this.videoCount = videoCount;
    }

    /**
     * Gets the list of video IDs associated with the channel.
     *
     * @return the list of video IDs
     */
    public List<String> getVideoIds() {
        return videoIds;
    }

    /**
     * Sets the list of video IDs associated with the channel.
     *
     * @param videoIds the new list of video IDs
     */
    public void setVideoIds(List<String> videoIds) {
        this.videoIds = videoIds;
    }
}
