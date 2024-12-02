package models.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static models.api.AnalyseSentiment.IGNORE_STOP_WORDS;

/**
 * The {@code AnalyseWords} class provides functionality for analyzing word statistics
 * from video descriptions. It filters out stop words, numbers, and symbols, and counts
 * the frequency of remaining words.
 *
 */
public class AnalyseWords {

    /**
     * Array of words representing numbers to be ignored during analysis.
     */
    public final static String[] NUMBER_WORDS = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

    /**
     * Array of symbols to be ignored during analysis.
     */
    public final static String[] SYMBOLS = {"!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "+", "=", "{", "}", "[", "]", "|", "\\", ":", ";", "\"", "'", "<", ">", ",", ".", "?", "/", "`", "~"};

    /**
     * Analyzes the word frequency in a list of video descriptions.
     * <p>
     * This method processes the list of descriptions in parallel, filters out stop words,
     * numbers, and symbols, and then counts the frequency of each word in the descriptions.
     * </p>
     *
     * @param videoDescriptions the list of video descriptions to analyze
     * @return a map where the keys are words and the values are their respective frequencies
     */
    public static Map<String, Integer> analyzeWordStats(List<String> videoDescriptions) {
        return videoDescriptions.parallelStream()
                .map(AnalyseWords::processWordStats)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Integer::sum,
                        HashMap::new
                ));
    }

    /**
     * Processes the word statistics for a single video description by filtering out
     * stop words, numbers, and symbols, and counting the remaining words.
     *
     * @param description the video description to process
     * @return a map where the keys are words and the values are their frequencies (1 for each occurrence)
     */
    private static Map<String, Integer> processWordStats(String description) {
        // Remove stop words
        String[] words = description.toUpperCase().split("\\s+");
        String[] filteredWords = java.util.Arrays.stream(words)
                .filter(word -> !contains(IGNORE_STOP_WORDS, word) && !contains(NUMBER_WORDS, word) && !contains(SYMBOLS, word))
                .toArray(String[]::new);

        return java.util.Arrays.stream(filteredWords)
                .collect(Collectors.toMap(
                        word -> word,
                        word -> 1,
                        Integer::sum,
                        HashMap::new
                ));
    }

    /**
     * Checks if a given value exists in an array.
     *
     * @param array the array to check
     * @param value the value to search for
     * @return {@code true} if the value is found in the array, {@code false} otherwise
     */
    private static boolean contains(String[] array, String value) {
        for (String item : array) {
            if (item.equals(value)) {
                return true;
            }
        }
        return false;
    }
}
