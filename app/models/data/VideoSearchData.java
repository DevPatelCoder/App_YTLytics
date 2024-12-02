package models.data;

import java.util.List;
import java.util.Map;

/**
 * Represents the data for a video search query, including the search query itself,
 * a list of videos that match the search, sentiment analysis, and word count statistics
 * for the query.
 *
 * @author Anubhav Mahajan
 * @author Arpnik Singh
 * @version 1.0
 */
public class VideoSearchData {

    public double getAvgFleshGrad() {
        return avgFleshGrad;
    }

    private final double avgFleshGrad;

    /**
     * Gets the average Flesch reading ease score of the video descriptions.
     *
     * @return The average Flesch reading ease score.
     */
    public double getAvgFleshScore() {
        return avgFleshScore;
    }

    private final double avgFleshScore;

//    public double getFleshGrad() {
//        return FleshGrad;
//    }
//
//    private final double FleshGrad;
//
//    public double getFleshScor() {
//        return FleshScor;
//    }
//
//    private final double FleshScor;

    /** The search query string. */
    private String query;

    /** The list of videos returned by the search. */
    private List<VideoData> videos;

    /** The word count statistics for the search query. */
    private Map<String, Integer> wordCount;

    /** The sentiment analysis result for the search query. */
    private Sentiment sentiment;

    /**
     * Constructs a new VideoSearchData object with the specified details.
     *
     * @param query the search query string
     * @param videos the list of video data matching the query
     * @param sentiment the sentiment analysis result for the query
     * @param wordCount the word count statistics for the query
     * @author Anubhav Mahajan
     * @author Arpnik Singh
     */
    public VideoSearchData(String query, List<VideoData> videos, Sentiment sentiment,double avgFleshGrad, double avgFleshScore, Map<String, Integer> wordCount) {
        this.query = query;
        this.videos = videos;
        this.sentiment = sentiment;
        this.avgFleshGrad = avgFleshGrad;
        this.avgFleshScore = avgFleshScore;
        this.wordCount = wordCount;
    }

    /**
     * Gets the search query string.
     *
     * @return the search query
     * @author Anubhav Mahajan
     */
    public String getQuery() {
        return query;
    }


    /**
     * Gets the list of videos returned by the search.
     *
     * @return the list of video data
     * @author Anubhav Mahajan
     */
    public List<VideoData> getVideos() {
        return videos;
    }

    /**
     * Gets the word count statistics for the search query.
     *
     * @return the word count map
     * @author Anubhav Mahajan
     */
    public Map<String, Integer> getWordCount() {
        return wordCount;
    }

    /**
     * Sets the word count statistics for the search query.
     *
     * @param wordCount the new word count map
     * @author Arpnik Singh
     */
    public void setWordCount(Map<String, Integer> wordCount) {
        this.wordCount = wordCount;
    }

    /**
     * Gets the sentiment analysis result for the search query.
     *
     * @return the sentiment result
     * @author Arpnik Singh
     */
    public Sentiment getSentiment() {
        return sentiment;
    }

}
