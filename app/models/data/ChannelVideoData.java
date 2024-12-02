package models.data;

import java.util.List;

/**
 * Represents a container for YouTube channel video data and channel metadata.
 * It holds a list of video data objects and the associated channel metadata.
 *
 */
public class ChannelVideoData {

    /** A list of video data associated with the channel. */
    private List<VideoData> videoDataList;

    /** The metadata for the channel. */
    private ChannelMetaData channelData;

    /**
     * Constructs a new ChannelVideoData object with the provided list of video data and channel metadata.
     *
     * @param videoDataList the list of video data objects
     * @param channelData the metadata of the channel
     */
    public ChannelVideoData(List<VideoData> videoDataList, ChannelMetaData channelData) {
        this.videoDataList = videoDataList;
        this.channelData = channelData;
    }

    /**
     * Gets the list of video data associated with the channel.
     *
     * @return the list of video data
     */
    public List<VideoData> getVideoDataList() {
        return videoDataList;
    }

    /**
     * Gets the metadata of the channel.
     *
     * @return the channel metadata
     */
    public ChannelMetaData getChannelData() {
        return channelData;
    }
}
