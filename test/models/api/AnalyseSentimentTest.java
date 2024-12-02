package models.api;

import models.data.Sentiment;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * This class contains unit tests for the AnalyseSentiment class.
 * It tests the functionality of sentiment analysis methods with different inputs.
 *
 * @author Arpnik Singh
 */
public class AnalyseSentimentTest {

    /**
     * Test case for counting sentiment words and detecting happy sentiment.
     */
    @Test
    public void testCountSentimentWords() {
        String description = "I am excited to see you today.";
        Sentiment sentiment = AnalyseSentiment.analyzeStreamSentiment(Arrays.asList(description));
        assertEquals(Sentiment.HAPPY, sentiment);
    }

    /**
     * Test case to detect happy sentiment in a string with happy words and emoticons.
     */
    @Test
    public void testDetectHappySentiment() {
        String description = "I am happy 😊 and excited 😀 to see you today.";
        Sentiment sentiment = AnalyseSentiment.analyzeStreamSentiment(Arrays.asList(description));
        assertEquals(Sentiment.HAPPY, sentiment);
    }

    /**
     * Test case to detect happy sentiment from emoticons.
     */
    @Test
    public void testDetectHappyEmoticon() {
        String description = "😊 😀";
        Sentiment sentiment = AnalyseSentiment.analyzeStreamSentiment(Arrays.asList(description));
        assertEquals(Sentiment.HAPPY, sentiment);
    }

    /**
     * Test case to detect sad sentiment.
     */
    @Test
    public void testDetectSadSentiment() {
        String description = "I am a bit sad 😞 about the weather.";
        Sentiment sentiment = AnalyseSentiment.analyzeStreamSentiment(Arrays.asList(description));
        assertEquals(Sentiment.SAD, sentiment);
    }

    /**
     * Test case to detect neutral sentiment.
     */
    @Test
    public void testDetectNeutralSentiment() {
        String description = "I am feeling okay today even though the weather is not good";
        Sentiment sentiment = AnalyseSentiment.analyzeStreamSentiment(Arrays.asList(description));
        assertEquals(Sentiment.NEUTRAL, sentiment);
    }

    /**
     * Test case for mixed sentiments with both happy and sad emoticons.
     */
    @Test
    public void testMixedSentimentEmoticon() {
        List<String> desc = Arrays.asList("😊", "😀", "😞", "😄");
        Sentiment sentiment = AnalyseSentiment.analyzeStreamSentiment(desc);
        assertEquals(Sentiment.HAPPY, sentiment);
    }

    /**
     * Test case for sad emoticons.
     */
    @Test
    public void testSadEmoticon() {
        List<String> desc = Arrays.asList("😞", "😟", "😔");
        Sentiment sentiment = AnalyseSentiment.analyzeStreamSentiment(desc);
        assertEquals(Sentiment.SAD, sentiment);
    }

    /**
     * Test case for mixed sentiment response with various emotions.
     * The description includes neutral, happy, and sad sentiments.
     */
    @Test
    // 1 neutral, 1 happy, rest sad
    public void testMixedSentimentResponse() {
        List<String> desc = Arrays.asList("I am feeling okay today even though the weather is not good",
                "I am depressed to see you today.",
                "Awful song. Never watch this",
                "#suffering #pain #sadness");
        Sentiment sentiment = AnalyseSentiment.analyzeStreamSentiment(desc);
        assertEquals(Sentiment.SAD, sentiment);
    }

    /**
     * Test case for sad emoticons using Unicode.
     */
    @Test
    public void testSadnicodeEmoticons() {
        List<String> desc = Arrays.asList("I'm feeling \ud83D\uDE1E");
        Sentiment sentiment = AnalyseSentiment.analyzeStreamSentiment(desc);
        assertEquals(Sentiment.SAD, sentiment);
    }

    /**
     * Test case for happy emoticons using Unicode.
     */
    @Test
    public void testHappyUnicodeEmoticon() {
        List<String> desc = Arrays.asList("I'm feeling \ud83D\uDE00");
        Sentiment sentiment = AnalyseSentiment.analyzeStreamSentiment(desc);
        assertEquals(Sentiment.HAPPY, sentiment);
    }
}
