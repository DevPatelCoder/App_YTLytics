package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.Patterns;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.data.Constants;
import models.data.VideoSearchData;
import scala.concurrent.Future;
import java.util.Map;
import javax.inject.Inject;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CompletionStage;

import static akka.pattern.Patterns.pipe;
import static scala.compat.java8.FutureConverters.toJava;

/**
 * The {@code UserActor} class represents an Akka actor responsible for managing user interactions
 * and handling WebSocket communication for search operations.
 *
 * <p>This actor communicates with a {@code CacheManagerActor} to fetch search results for user-provided
 * search terms and sends responses back to the WebSocket. It also manages a list of recent search terms
 * and periodically refreshes the cache for those searches using a timer.
 */
public class UserActor extends AbstractActor {
    /**
     * Logger instance for logging information and errors related to {@code UserActor}.
     */
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    /**
     * Queue to store recent search terms entered by the user.
     */
    private final Queue<String> recentSearches = new LinkedList<>();
    /**
     * Actor reference for WebSocket communication.
     */
    private final ActorRef webSocketOut;
    /**
     * Actor reference for the {@code CacheManagerActor}.
     */
    private final ActorRef cacheManagerActor;
    /**
     * Actor reference for the timer actor used for periodic cache refresh operations.
     */
    private ActorRef timerActor;

    /**
     * Message type used to trigger cache refresh operations.
     */
    public static class RefreshCache {}

    /**
     * Invoked when the actor starts. Initializes and starts the timer actor for periodic cache refresh.
     */
    @Override
    public void preStart() {
        try {
            timerActor = getContext().actorOf(
                    UserRefreshTimerActor.props(),
                    "user-cache-refresh-timer"
            );

            // Start the refresh timer with 10-second interval
            timerActor.tell(
                    new UserRefreshTimerActor.StartTimer("userSearchRefresh", Constants.REFRESH_USER_PAGE_RATE_IN_SECONDS),
                    getSelf()
            );
        } catch (Exception e) {
            log.error(e, "Error starting timer actor: {}", e.getMessage());
            throw e; // Let supervisor handle this
        }
    }

    /**
     * Factory method for creating {@code Props} for this actor.
     *
     * @param out              The WebSocket actor for communication.
     * @param cacheManagerActor The actor managing cached search results.
     * @return A {@code Props} instance for creating a {@code UserActor}.
     */
    public static Props props(ActorRef out, ActorRef cacheManagerActor) {
        return Props.create(UserActor.class, out, cacheManagerActor);
    }

    /**
     * Constructs a {@code UserActor}.
     *
     * @param webSocketOut      The WebSocket actor for communication.
     * @param cacheManagerActor The actor managing cached search results.
     */
    @Inject
    public UserActor(ActorRef webSocketOut, ActorRef cacheManagerActor) {
        this.webSocketOut = webSocketOut;
        this.cacheManagerActor = cacheManagerActor;
    }

    /**
     * Refreshes the cache for recent search terms by sending requests to the {@code CacheManagerActor}.
     */
    private void refreshRecentSearches() {
        log.info("Starting cache refresh for {} recent searches", recentSearches.size());
        for (String searchTerm : recentSearches) {

            try {
//                searchTerm = searchTerm + " testingd"; //TODO remove this line
                Future<Object> future = Patterns.ask(cacheManagerActor,
                        new CacheManagerActor.SearchRequest(searchTerm),
                        60000
                );

                pipe(toJava(future)
                                .thenApply(this::processSearchResult)
                                .exceptionally(this::handleSearchError),
                        getContext().dispatcher())
                        .to(webSocketOut);
            } catch (Exception e) {
                log.error(e, "Unexpected error during search refresh: {}", e.getMessage());
                // Send error message to WebSocket
                sendErrorToWebSocket("Search refresh failed: " + e.getMessage());
            }
        }
    }

    /**
     * Processes the search result obtained from the {@code CacheManagerActor}.
     *
     * @param result The search result object.
     * @return A JSON string representation of the result, or an error message if processing fails.
     */
    private String processSearchResult(Object result) {
        try {
            if (result instanceof VideoSearchData) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.writeValueAsString(result);
            }
            return result.toString();
        } catch (Exception e) {
            log.error("Error processing search result", e);
            return "Error processing search result";
        }
    }

    /**
     * Handles errors that occur during the search process.
     *
     * @param throwable The error that occurred.
     * @return A string representing the error message.
     */
    private String handleSearchError(Throwable throwable) {
        log.error(throwable, "Error in search process: {}", throwable.getMessage());
        return "Search error: " + throwable.getMessage();
    }

    /**
     * Sends an error message to the WebSocket in JSON format.
     *
     * @param errorMessage The error message to be sent.
     */
    private void sendErrorToWebSocket(String errorMessage) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonError = mapper.writeValueAsString(
                    Map.of("error", errorMessage)
            );
            webSocketOut.tell(jsonError, getSelf());
        } catch (Exception e) {
            log.error(e, "Could not send error to WebSocket");
        }
    }

    /**
     * Defines the message handling behavior of this actor.
     *
     * <p>Handles:
     * <ul>
     *     <li>{@code String}: Represents a search term entered by the user.</li>
     *     <li>{@code RefreshCache}: Triggers the cache refresh operation.</li>
     *     <li>Any other message: Logs a warning for unrecognized messages.</li>
     * </ul>
     *
     * @return The {@code Receive} instance defining the actor's behavior.
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, searchTerm -> {
                    try {
                        // Store search term
                        if (recentSearches.size() >= Constants.MAX_VIDEOS_DISPLAY_COUNT) {
                            recentSearches.poll();
                        }
                        recentSearches.offer(searchTerm);

                        // Send initial search request to CacheManagerActor
                        Future<Object> future = Patterns.ask(cacheManagerActor,
                                new CacheManagerActor.SearchRequest(searchTerm),
                                60000
                        );

                        pipe(toJava(future)
                                        .thenApply(this::processSearchResult)
                                        .exceptionally(this::handleSearchError),
                                getContext().dispatcher())
                                .to(webSocketOut);
                    } catch (Exception e) {
                        log.error(e, "Error processing search term: {}", e.getMessage());
                        sendErrorToWebSocket("Search processing failed: " + e.getMessage());
                    }
                })
                // Add handler for TimerTick
                .match(RefreshCache.class, msg -> refreshRecentSearches())
                .matchAny(msg -> {
                    log.warning("Received unknown message: {}", msg);
                })
                .build();
    }

    /**
     * Invoked when the actor is stopped. Stops the timer actor if it exists.
     */
    @Override
    public void postStop() {
        // Stop the timer when the UserActor is stopped
        if (timerActor != null) {
            timerActor.tell(
                    new UserRefreshTimerActor.StopTimer("userSearchRefresh"),
                    getSelf()
            );
        }
        log.info("UserActor is stopping");
    }
}