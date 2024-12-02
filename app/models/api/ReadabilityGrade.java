package models.api;

import java.util.List;
import java.util.stream.Collectors;
/**
 * This class provides methods to calculate readability scores for a given text using the
 * Flesch-Kincaid Grade Level and the Flesch Reading Ease formulas. It also includes utility methods
 * for counting words, sentences, and syllables in the text, as well as calculating average scores
 * for multiple pieces of text.
 */
public class ReadabilityGrade {
    /**
     * Calculates the Flesch-Kincaid Grade Level for a given text.
     * The Flesch-Kincaid Grade Level formula estimates the U.S. school grade level required
     * to understand the text.
     *
     * @param text The text to analyze.
     * @return The Flesch-Kincaid Grade Level score rounded to two decimal places.
     */
    public static double calculateFleschKincaidGradeLevel(String text) {

        int wordCount = countWords(text);
        int sentenceCount = countSentences(text);
        int syllableCount = countSyllables(text);

        if (wordCount == 0 || sentenceCount == 0) {
            return 0.0; // Avoid division by zero
        }

        double value = 0.39 * ((double) wordCount / sentenceCount) + 11.8 * ((double) syllableCount / wordCount) - 15.59;
        return Math.round(value * 100.0) / 100.0;
//        return 0.39 * ((double) wordCount / sentenceCount) + 11.8 * ((double) syllableCount / wordCount) - 15.59;
    }

    /**
     * Calculates the Flesch Reading Ease Score for a given text.
     * The Flesch Reading Ease formula measures how easy the text is to understand,
     * with higher scores indicating easier readability.
     *
     * @param text The text to analyze.
     * @return The Flesch Reading Ease score rounded to two decimal places.
     */
    public static double calculateFleschReadingEase(String text) {
        int wordCount = countWords(text);
        int sentenceCount = countSentences(text);
        int syllableCount = countSyllables(text);

        if (wordCount == 0 || sentenceCount == 0) {
            return 0.0; // Avoid division by zero
        }

        double value = 206.835 - 1.015 * ((double) wordCount / sentenceCount) - 84.6 * ((double) syllableCount / wordCount);
        return Math.round(value * 100.0) / 100.0;

//        return 206.835 - 1.015 * ((double) wordCount / sentenceCount) - 84.6 * ((double) syllableCount / wordCount);
    }
    /**
     * Counts the number of words in a given text.
     *
     * @param text The text to analyze.
     * @return The word count.
     */
    // Helper methods
//    static int countWords(String text) {
//        return text.trim().split("\\s+").length;
//    }
    static int countWords(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        String[] words = text.trim().split("\\b\\w+\\b");
        return words.length;
    }

    /**
     * Counts the number of sentences in a given text based on punctuation marks.
     *
     * @param text The text to analyze.
     * @return The sentence count.
     */
    static int countSentences(String text) {
        return text.split("[.!?]").length;
    }
    /**
     * Counts the number of syllables in a given word.
     * The method assumes English phonetics and uses vowels to estimate syllable counts.
     *
     * @param word The word to analyze.
     * @return The syllable count.
     */
    static int countSyllables(String word) {
        String vowels = "aeiouy";
        word = word.toLowerCase().replaceAll("[^a-z]", "");
        int syllableCount = 0;
        boolean lastWasVowel = false;

        for (char c : word.toCharArray()) {
            boolean isVowel = vowels.indexOf(c) != -1;
            if (isVowel && !lastWasVowel) {
                syllableCount++;
            }
            lastWasVowel = isVowel;
        }

        if (word.endsWith("e") && syllableCount > 1) {
            syllableCount--;
        }
        return syllableCount > 0 ? syllableCount : 1;
    }

    /**
     * Calculates the average Flesch-Kincaid Grade Level for a list of texts.
     *
     * @param texts A list of texts to analyze.
     * @return The average Flesch-Kincaid Grade Level score rounded to two decimal places.
     */
    public static double calculateAverageFleschKincaid(List<String> texts) {
        double average = texts.stream()
                .mapToDouble(ReadabilityGrade::calculateFleschKincaidGradeLevel)
                .average()
                .orElse(0.0);
        return Math.round(average * 100.0)/ 100.0;
    }

    /**
     * Calculates the average Flesch Reading Ease Score for a list of texts.
     *
     * @param texts A list of texts to analyze.
     * @return The average Flesch Reading Ease score rounded to two decimal places.
     */
    public static double calculateAverageFleschReadingEase(List<String> texts) {
        double average = texts.stream()
                .mapToDouble(ReadabilityGrade::calculateFleschReadingEase)
                .average()
                .orElse(0.0);
        return Math.round(average * 100.0)/ 100.0;

    }
}

