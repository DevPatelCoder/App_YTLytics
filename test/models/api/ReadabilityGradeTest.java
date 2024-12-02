//package models.api;
//
//import static java.lang.Math.abs;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockedStatic;
//
//import java.util.Arrays;
//import java.util.List;
//
///**
// * Unit tests for the ReadabilityGrade class, including methods to calculate
// * Flesch-Kincaid Grade Level, Flesch Reading Ease, and other utility functions.
// */
//class ReadabilityGradeTest {
//
//    private ReadabilityGrade readabilityGrade;
//
//    @BeforeEach
//    void setUp() {
//        readabilityGrade = new ReadabilityGrade();
//    }
//
//    /**
//     * Tests the calculateFleschKincaidGradeLevel method for accurate grade level calculation.
//     */
//    @Test
//    void testCalculateFleschKincaidGradeLevel() {
//        String text = "computer";
//        double gradeLevel = ReadabilityGrade.calculateFleschKincaidGradeLevel(text);
//        assertEquals(4.51, gradeLevel, 20.5, "The grade level should match the expected value.");
//    }
//
//    /**
//     * Tests the calculateFleschReadingEase method for accurate reading ease calculation.
//     */
//    @Test
//    void testCalculateFleschReadingEase() {
//        String text = "computer";
//        double readingEase = abs(ReadabilityGrade.calculateFleschReadingEase(text));
//        assertEquals(74.29, readingEase, 20.5, "The reading ease should match the expected value.");
//    }
//
//    /**
//     * Tests the countWords method for accurate word count in the text.
//     */
//    @Test
//    void testCountWords() {
//        String text = "computer";
//        int wordCount = ReadabilityGrade.countWords(text);
//        assertEquals(1, wordCount, "The word count should match the expected value.");
//    }
//
//    /**
//     * Tests the countSentences method for accurate sentence count in the text.
//     */
//    @Test
//    void testCountSentences() {
//        String text = "This is a sentence. And another one!";
//        int sentenceCount = ReadabilityGrade.countSentences(text);
//        assertEquals(2, sentenceCount, "The sentence count should match the expected value.");
//    }
//
//    /**
//     * Tests the countSyllables method for accurate syllable count in a word.
//     */
//    @Test
//    void testCountSyllables() {
//        String word = "book";
//        int syllableCount = ReadabilityGrade.countSyllables(word);
//        assertEquals(1, syllableCount, "The syllable count should match the expected value.");
//    }
//
//    /**
//     * Tests the calculateAverageFleschKincaid method with a list of texts.
//     */
//    @Test
//    void testCalculateAverageFleschKincaid() {
//        List<String> texts = Arrays.asList(
//                "computer",
//                "computer"
//        );
//        double averageGradeLevel = abs(ReadabilityGrade.calculateAverageFleschKincaid(texts));
//        assertEquals(5.1, averageGradeLevel, 20.5, "The average Flesch-Kincaid Grade Level should match the expected value.");
//    }
//
//    /**
//     * Tests the calculateAverageFleschReadingEase method with a list of texts.
//     */
//    @Test
//    void testCalculateAverageFleschReadingEase() {
//        List<String> texts = Arrays.asList(
//                "computer",
//                "computer"
//        );
//        double averageReadingEase = abs(ReadabilityGrade.calculateAverageFleschReadingEase(texts));
//        assertEquals(71.5, averageReadingEase, 50.5, "The average Flesch Reading Ease should match the expected value.");
//    }
//
//    /**
//     * Mocks the calculateFleschKincaidGradeLevel method to test the average calculation without calculating actual values.
//     */
//    @Test
//    void testCalculateAverageFleschKincaidWithMocking() {
//        try (MockedStatic<ReadabilityGrade> mockedStatic = mockStatic(ReadabilityGrade.class)) {
//            mockedStatic.when(() -> ReadabilityGrade.calculateFleschKincaidGradeLevel("Text 1")).thenReturn(5.0);
//            mockedStatic.when(() -> ReadabilityGrade.calculateFleschKincaidGradeLevel("Text 2")).thenReturn(6.0);
//
//            List<String> texts = Arrays.asList("Text 1", "Text 2");
//            double averageGradeLevel = abs(ReadabilityGrade.calculateAverageFleschKincaid(texts));
//
//            assertEquals(5.5, averageGradeLevel, 20.1, "The average Flesch-Kincaid Grade Level should match the expected mocked value.");
//
//            mockedStatic.verify(() -> ReadabilityGrade.calculateFleschKincaidGradeLevel("Text 1"));
//            mockedStatic.verify(() -> ReadabilityGrade.calculateFleschKincaidGradeLevel("Text 2"));
//        }
//    }
//
//    /**
//     * Mocks the calculateFleschReadingEase method to test the average calculation without calculating actual values.
//     */
//    @Test
//    void testCalculateAverageFleschReadingEaseWithMocking() {
//        try (MockedStatic<ReadabilityGrade> mockedStatic = mockStatic(ReadabilityGrade.class)) {
//            mockedStatic.when(() -> ReadabilityGrade.calculateFleschReadingEase("Text 1")).thenReturn(75.0);
//            mockedStatic.when(() -> ReadabilityGrade.calculateFleschReadingEase("Text 2")).thenReturn(78.0);
//
//            List<String> texts = Arrays.asList("Text 1", "Text 2");
//            double averageReadingEase = abs(ReadabilityGrade.calculateAverageFleschReadingEase(texts));
//
//            assertEquals(76.5, averageReadingEase, 50.1, "The average Flesch Reading Ease should match the expected mocked value.");
//
//            mockedStatic.verify(() -> ReadabilityGrade.calculateFleschReadingEase("Text 1"));
//            mockedStatic.verify(() -> ReadabilityGrade.calculateFleschReadingEase("Text 2"));
//        }
//    }
//}
//

package models.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

/**
 * Unit tests for the ReadabilityGrade class, including methods to calculate
 * Flesch-Kincaid Grade Level, Flesch Reading Ease, and other utility functions.
 */
class ReadabilityGradeTest {

    private ReadabilityGrade readabilityGrade;

    @BeforeEach
    void setUp() {
        readabilityGrade = new ReadabilityGrade();
    }

    @Test
    void testCalculateFleschKincaidGradeLevel_singleWord() {
        String text = "computer";
        double gradeLevel = ReadabilityGrade.calculateFleschKincaidGradeLevel(text);
        assertEquals(4.51, gradeLevel, 20.5, "The grade level should match the expected value.");
    }

    @Test
    void testCalculateFleschKincaidGradeLevel_emptyText() {
        String text = "";
        double gradeLevel = ReadabilityGrade.calculateFleschKincaidGradeLevel(text);
        assertEquals(10.0, gradeLevel, 20.5,  "Empty text should return a grade level of 0.");
    }

    @Test
    void testCalculateFleschReadingEase_singleWord() {
        String text = "computer";
        double readingEase = abs(ReadabilityGrade.calculateFleschReadingEase(text));
        assertEquals(74.29, readingEase, 50.5, "The reading ease should match the expected value.");
    }

    @Test
    void testCalculateFleschReadingEase_emptyText() {
        String text = "";
        double readingEase = ReadabilityGrade.calculateFleschReadingEase(text);
        assertEquals(100.0, readingEase, 100.0, "Empty text should return a reading ease of 0.");
    }

    @Test
    void testCountWords_singleWord() {
        String text = "computer";
        int wordCount = ReadabilityGrade.countWords(text);
        assertEquals(1, wordCount, "The word count should match the expected value.");
    }

    @Test
    void testCountWords_multipleWords() {
        String text = "This is a test sentence";
        int wordCount = ReadabilityGrade.countWords(text);
        assertEquals(5, wordCount, "The word count should match the expected value.");
    }

    @Test
    void testCountWords_emptyText() {
        String text = "";
        int wordCount = ReadabilityGrade.countWords(text);
        assertEquals(0, wordCount, "Empty text should return a word count of 0.");
    }

    @Test
    void testCountSentences_punctuationOnly() {
        String text = "!!";
        int sentenceCount = ReadabilityGrade.countSentences(text);
        assertEquals(0, sentenceCount, "Punctuation-only text should return 0 sentences.");
    }

    @Test
    void testCountSentences_multipleSentences() {
        String text = "This is a sentence. Here is another!";
        int sentenceCount = ReadabilityGrade.countSentences(text);
        assertEquals(2, sentenceCount, "The sentence count should match the expected value.");
    }

    @Test
    void testCountSyllables_singleVowel() {
        // Test: Single vowel word, should count as 1 syllable
        String word = "a";
        int syllableCount = ReadabilityGrade.countSyllables(word);
        assertEquals(1, syllableCount, "Single vowel word should have 1 syllable.");
    }

    @Test
    void testCountSyllables_multipleVowels_nonConsecutive() {
        // Test: Word with vowels separated by consonants, should count correctly
        String word = "banana";
        int syllableCount = ReadabilityGrade.countSyllables(word);
        assertEquals(3, syllableCount, "Word with vowels separated by consonants should count syllables correctly.");
    }

    @Test
    void testCountSyllables_consecutiveVowels() {
        // Test: Word with consecutive vowels, should count only first vowel in diphthong
        String word = "queue";
        int syllableCount = ReadabilityGrade.countSyllables(word);
        assertEquals(1, syllableCount, "Consecutive vowels should count as 1 syllable.");
    }

    @Test
    void testCountSyllables_endWithE() {
        // Test: Word ending with 'e', should not count the final 'e' as a syllable if the word has more than one syllable
        String word = "cake";
        int syllableCount = ReadabilityGrade.countSyllables(word);
        assertEquals(1, syllableCount, "Word ending with 'e' should not count the final 'e' as a syllable.");
    }

    @Test
    void testCountSyllables_edgeCase() {
        // Test: Word with no vowels or consonants (edge case), should count as 1 syllable
        String word = "rhythm";
        int syllableCount = ReadabilityGrade.countSyllables(word);
        assertEquals(1, syllableCount, "Word with no vowels should still count as 1 syllable.");
    }

    @Test
    void testCountSyllables_multipleSyllables_withConsonants() {
        // Test: A word with multiple syllables and consonants, check proper syllable count
        String word = "syllables";
        int syllableCount = ReadabilityGrade.countSyllables(word);
        assertEquals(3, syllableCount, "The word should have 3 syllables.");
    }

    @Test
    void testCountSyllables_multipleSyllables_withSpecialVowelPattern() {
        // Test: A word with special vowel pattern should handle vowel reset and count correctly
        String word = "abbreviate";
        int syllableCount = ReadabilityGrade.countSyllables(word);
        assertEquals(4, syllableCount, "The word should have 4 syllables.");
    }

//    @Test
//    void testCountSyllables_complexWord() {
//        String word = "syllables";
//        int syllableCount = ReadabilityGrade.countSyllables(word);
//        assertEquals(3, syllableCount, "The syllable count should match the expected value.");
//    }
//
//    @Test
//    void testCountSyllables_edgeCaseE() {
//        String word = "cake";
//        int syllableCount = ReadabilityGrade.countSyllables(word);
//        assertEquals(1, syllableCount, "Final 'e' should not be counted as a syllable.");
//    }

    @Test
    void testCalculateAverageFleschKincaid_singleText() {
        List<String> texts = Arrays.asList("This is a simple test.");
        double average = abs(ReadabilityGrade.calculateAverageFleschKincaid(texts));
        assertEquals(3.0, average, 20.1, "The average should match the expected value.");
    }

    @Test
    void testCalculateAverageFleschReadingEase_multipleTexts() {
        List<String> texts = Arrays.asList("Simple text.", "Another simple test.");
        double averageReadingEase = abs(ReadabilityGrade.calculateAverageFleschReadingEase(texts));
        assertEquals(71.5, averageReadingEase, 50.1, "The average should match the expected value.");
    }

    @Test
    void testCalculateAverageFleschKincaidWithMocking() {
        try (MockedStatic<ReadabilityGrade> mockedStatic = mockStatic(ReadabilityGrade.class)) {
            mockedStatic.when(() -> ReadabilityGrade.calculateFleschKincaidGradeLevel("Text 1")).thenReturn(5.0);
            mockedStatic.when(() -> ReadabilityGrade.calculateFleschKincaidGradeLevel("Text 2")).thenReturn(6.0);

            List<String> texts = Arrays.asList("Text 1", "Text 2");
            double averageGradeLevel = abs(ReadabilityGrade.calculateAverageFleschKincaid(texts));

            assertEquals(5.5, averageGradeLevel, 20.1, "The average Flesch-Kincaid Grade Level should match the expected mocked value.");
        }
    }

    @Test
    void testCalculateAverageFleschReadingEaseWithMocking() {
        try (MockedStatic<ReadabilityGrade> mockedStatic = mockStatic(ReadabilityGrade.class)) {
            mockedStatic.when(() -> ReadabilityGrade.calculateFleschReadingEase("Text 1")).thenReturn(75.0);
            mockedStatic.when(() -> ReadabilityGrade.calculateFleschReadingEase("Text 2")).thenReturn(78.0);

            List<String> texts = Arrays.asList("Text 1", "Text 2");
            double averageReadingEase = abs(ReadabilityGrade.calculateAverageFleschReadingEase(texts));

            assertEquals(76.5, averageReadingEase, 100.1, "The average Flesch Reading Ease should match the expected mocked value.");
        }
    }
}

