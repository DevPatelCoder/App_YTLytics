package models.data;

/**
 * This class holds the constant values used throughout the application.
 * These constants define various limits and thresholds for video display count,
 * sentiment analysis, and sentiment threshold.
 *
 */
public class Constants {

    /** The maximum number of videos to display. */
    public static final int MAX_VIDEOS_DISPLAY_COUNT = 10;

    /** The maximum number of sentiment counts in a description. */
    public static final int MAX_DESC_SENTIMENT_COUNT = 50;

    /** The threshold value for sentiment analysis. */
    public static final double SENTIMENT_THRESHOLD = 0.7;

    public static final long REFRESH_RATE_IN_SECONDS = 10;

    public static final long REFRESH_USER_PAGE_RATE_IN_SECONDS = 20;

}
