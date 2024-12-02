package models.api;

import models.data.Constants;
import models.data.Sentiment;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
/**
 * The AnalyseSentiment class is responsible for analyzing the sentiment of video descriptions.
 * It uses predefined sets of happy and sad words and emoticons to determine the sentiment of a given list of descriptions.
 * For a each description, if the ratio of count of happy word to happy+sad words is greater then 70% then the sentiment is considered as happy.
 * For a stream of videos, the overall sentiment is defined as happy if 10% of videos description are happy.
 *  If 10% of videos descriptions are sad, then the overall sentiment is considered as sad. And otherwise considered as neutral.
 */
public class AnalyseSentiment {

    private final static String[] HAPPY_WORDS = {  ":)", ";)", ":-)", ":D", "<3", "PARTY", "\uD83D\uDC97","\uD83D\uDC93","APPRECIATE","\uD83D\uDC9A","\uD83D\uDC4F","\uD83E\uDDE1","\uD83D\uDC9B","\uD83D\uDC9A","\uD83D\uDC9C","\uD83E\uDD70","\uD83D\uDDA4","GAY", "OVERJOYED","THRILLED","UPBEAT", "GREAT", "HAPPY", "JOY", "EXCITED", "LOVE", "AWESOME", "WONDERFUL", "EXCELLENT", "AMAZING", "FANTASTIC",
            "BRILLIANT", "POSITIVE", "DELIGHT", "PLEASED", "CONTENT", "SATISFIED", "CHEERFUL", "GLAD", "ENTHUSIASTIC", "GRATEFUL", "BLESSED", "HOPEFUL", "SMILE", "CELEBRATE", "ADMIRE", "VICTORY", "LAUGH", "JUBILANT", "OPTIMISTIC", "ECSTATIC", "ELATED", "CHARMING", "SUNNY", "RADIANT", "PEACEFUL", "HARMONIOUS", "LIGHTHEARTED", "UPLIFTED", "PROUD", "GRIN", "ADORED", "BRAVO", "FANTABULOUS", "GENIUS", "MASTERPIECE",
            "KIND", "SUPPORTIVE", "HEARTWARMING", "HILARIOUS", "PLAYFUL", "SPARKLE", "SERENE", "GORGEOUS", "INSPIRE", "MOTIVATE", "HOPE", "POSITIVITY", "UPLIFT", "POWER OF SELF-BELIEF", "DREAM BIG", "MINDSET", "GROWTH", "GRATITUDE", "OVERCOME CHALLENGES", "NEVER GIVE UP", "SUCCESS", "POSITIVE THINKING", "INNER PEACE", "FIND JOY"};

    private final static String[] SAD_WORDS = { "ANGRY", ":(", ";(", ":'(", ":-(",":(", "MAD", "DISAPPOINTMENT", "BAD DAY","\uD83D\uDCCA","\uD83D\uDE1E","\uD83D\uDE14", "SAD", "UNHAPPY", "TERRIBLE", "BAD", "AWFUL", "WORST", "PAIN", "SORROW", "REGRET", "MISERABLE", "DEPRESSING", "DISAPPOINT", "FAIL", "GLOOMY", "HURT", "CRY", "GRIEF", "LONELY", "HEARTBROKEN", "FRUSTRATED", "PATHETIC", "FEAR", "HATE", "RESENT", "DISTRESS",
            "TRAGIC", "DEVASTATED", "MELANCHOLY", "FROWN", "HOPELESS", "AGONIZING", "BITTER", "DESPAIR", "SUFFERING", "TORMENTED", "HELPLESS", "DISTURBED", "ASHAMED", "MOURN", "TORMENT", "BROKEN", "WEARY", "TEARFUL", "ANXIOUS", "PESSIMISTIC", "UPSET", "DETEST", "TORTURED", "OVERWHELMED", "CRUSHED", "DOOM", "TIRED", "FEARFUL", "REMORSE", "DOWNCAST", "DEMORALIZED", "MISERY", "REGRETFUL", "RUINED", "PAINFUL",
            "DEJECTED", "DISHEARTENED", "FORLORN", "DESPONDENT", "GLOOMY", "MOROSE", "DOWNHEARTED", "DISMAL", "SORROWFUL", "MOURNFUL", "DOLEFUL", "LAMENTABLE", "DESPAIRING", "DISCONSOLATE", "CRESTFALLEN", "WOEFUL", "INCONSOLABLE", "DESOLATE", "WRETCHED", "DISTRESSED", "ANGUISHED", "DISTRAUGHT", "DISPIRITED", "MELANCHOLIC", "HEAVY-HEARTED", "BROKENHEARTED", "DOWNTRODDEN", "CRESTFALLEN", "GRIM", "BLEAK", "DOLOROUS", "MOURNFUL", "DEJECTEDLY", "GLUMLY", "MISERABLY", "DESPONDENTLY", "DESOLATELY"};

    public final static String[] IGNORE_STOP_WORDS = {"A","THE","IS","ARE","AND","OR","BUT","NOT","TO","FOR","WITH","FROM","IN","ON","AT","OF","BY","AS","IF","WHEN","THEN","IT","THIS","THAT","WAS","WERE","HAS","HAVE","HAD","WHICH","WHAT","WHY","WHO","WHOM","HOW","SOME","ANY","MANY","MORE","MOST","OTHER","EACH","EVERY","ALL","NO","NOR","SO","CAN","WILL","WOULD","SHOULD","COULD","DID","DOES","MAY","MIGHT","MUST","SHALL","WILL"};

    public final static String[] HAPPY_EMOTICONS = {   "😊", "😀", "😄", "😃", "🙂", "😁", "😆", "😊", "😉", "😍", "😜", "😇", "😭", "💜", "😀", "😊", "😀"};
    private static final String[] SAD_EMOTICONS = {"😞", "😟", "😔", "😣", "😕"};

    public static Sentiment analyzeStreamSentiment(List<String> videoDescriptions) {
        List<Sentiment> sentiments = videoDescriptions.stream()
                .parallel()
                .map(AnalyseSentiment::countSentimentWords).map(AnalyseSentiment::checkSentiment).collect(Collectors.toList());
        long happyCount = sentiments.stream().filter(sentiment -> sentiment == Sentiment.HAPPY).count();
        long sadCount = sentiments.stream().filter(sentiment -> sentiment == Sentiment.SAD).count();

        double happyRatio = (double) happyCount / videoDescriptions.size();
        double sadRatio = (double) sadCount / videoDescriptions.size();

        if (happyRatio > 0.1) {
            return Sentiment.HAPPY;
        } else if (sadRatio > 0.1) {
            return Sentiment.SAD;
        } else {
            return Sentiment.NEUTRAL;
        }

    }
    /**
     * Analyzes the sentiment of a stream of video descriptions.
     * It processes the list of video descriptions to determine whether the overall sentiment is happy, sad, or neutral.
     *
     * @param videoDescriptions A list of video descriptions to analyze
     * @return The overall sentiment of the video descriptions (Happy, Sad, or Neutral)
     */

    private static SentimentCount countSentimentWords(String description) {
        String upperDescription = description.toUpperCase();

        // Count all occurrences of happy words
        long happyCount = Arrays.stream(HAPPY_WORDS)
                .mapToLong(word ->
                        Pattern.compile("\\b" + Pattern.quote(word) + "\\b")
                                .matcher(upperDescription)
                                .results()
                                .count())
                .sum();

        // Count all occurrences of sad words
        long sadCount = Arrays.stream(SAD_WORDS)
                .mapToLong(word ->
                        Pattern.compile("\\b" + Pattern.quote(word) + "\\b")
                                .matcher(upperDescription)
                                .results()
                                .count())
                .sum();

        // Count stop words
        long stopWordsCount = Arrays.stream(IGNORE_STOP_WORDS)
                .mapToLong(word ->
                        Pattern.compile("\\b" + Pattern.quote(word) + "\\b")
                                .matcher(upperDescription)
                                .results()
                                .count())
                .sum();

        long totalWords = Arrays.stream(upperDescription.split("\\b"))
                .filter(word -> word.matches("[A-Z]+"))
                .count() - stopWordsCount;
        return new SentimentCount(happyCount + countEmoticons(description, HAPPY_EMOTICONS), sadCount + countEmoticons(description, SAD_EMOTICONS),  totalWords);
    }
    /**
     * Counts the number of happy and sad words, emoticons, and stop words in a given video description.
     *
     * @param description The video description to analyze
     * @return A SentimentCount object containing the counts of happy, sad, and total words
     */

    private static class SentimentCount {
        long happyCount;
        long sadCount;
        long totalCount;

        SentimentCount(long happyCount, long sadCount, long totalCount) {
            this.happyCount = happyCount;
            this.sadCount = sadCount;
            this.totalCount = totalCount;

        }

    }
    /**
     * Determines the overall sentiment based on the happy and sad word counts.
     *
     * @param sentimentCount The sentiment counts for a given video description
     * @return The overall sentiment (HAPPY, SAD, or NEUTRAL)
     */
    private static Sentiment checkSentiment(SentimentCount count) {
        if(count.sadCount == 0 && count.happyCount == 0){
            return Sentiment.NEUTRAL;
        }

        double happyPercentage = (double) count.happyCount / (count.sadCount + count.happyCount); // Add 1 to avoid division by 0
        double sadPercentage = (double) count.sadCount / (count.sadCount + count.happyCount);

        if (happyPercentage > Constants.SENTIMENT_THRESHOLD) {
            return Sentiment.HAPPY;
        } else if (sadPercentage > Constants.SENTIMENT_THRESHOLD) {
            return Sentiment.SAD;
        } else {
            return Sentiment.NEUTRAL;
        }

    }
    /**
     * Counts the occurrences of emoticons in a given text.
     *
     * @param text The text to search for emoticons
     * @param emoticons The array of emoticons to count in the text
     * @return The number of occurrences of emoticons in the text
     */
    private static long countEmoticons(String text, String[] emoticons) {
        long count = 0;
        for (String emoticon : emoticons) {
            count += Pattern.compile(Pattern.quote(emoticon)).matcher(text).results().count();
        }
        return count;
    }

}
