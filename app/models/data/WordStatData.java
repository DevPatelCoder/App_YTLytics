package models.data;

import java.util.Map;

/**
 * Represents the word count statistics for a specific search query.
 * Contains the search query string and a map of word counts.
 *
 */
public class WordStatData {

    /** The search query string. */
    private String search;

    /** The map of words and their respective counts for the search query. */
    private Map<String, Integer> wordCount;

    /**
     * Constructs a new WordStatData object with the specified search query and word count map.
     *
     * @param search the search query string
     * @param wordCount the map of word counts
     */
    public WordStatData(String search, Map<String, Integer> wordCount) {
        this.search = search;
        this.wordCount = wordCount;
    }

    /**
     * Gets the search query string.
     *
     * @return the search query
     */
    public String getSearch() {
        return search;
    }

    /**
     * Gets the map of word counts for the search query.
     *
     * @return the map of word counts
     */
    public Map<String, Integer> getWordCount() {
        return wordCount;
    }
}
