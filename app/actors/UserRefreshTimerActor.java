package actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.concurrent.duration.Duration;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * The {@code UserRefreshTimerActor} class is an Akka actor responsible for managing periodic timers.
 * These timers notify the parent actor (e.g., {@code CacheManagerActor}) about timed events
 * such as cache refresh triggers.
 *
 * <p>The actor supports starting and stopping timers identified by unique names.
 * It uses Akka's scheduling mechanism to handle periodic tasks and ensures that
 * notifications are sent to the parent actor when a timer tick occurs.
 */
public class UserRefreshTimerActor extends AbstractActor{
    /**
     * Logger instance for logging information and debugging related to {@code UserRefreshTimerActor}.
     */
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    /**
     * A map to track the last refresh times of timers, keyed by their unique names.
     */
    private final Map<String, Instant> lastRefreshTimes = new HashMap<>();

    /**
     * Message to start a timer with a specified interval.
     */
    public static class StartTimer {
        /**
         * The interval in seconds between timer ticks.
         */
        private final long intervalSeconds;
        /**
         * The unique name of the timer.
         */
        private final String timerName;

        public StartTimer(String timerName, long intervalSeconds) {
            this.timerName = timerName;
            /**
             * Constructs a {@code StartTimer} message.
             *
             * @param timerName     The name of the timer.
             * @param intervalSeconds The interval in seconds between timer ticks.
             */
            this.intervalSeconds = intervalSeconds;
        }
    }

    /**
     * Message to stop a timer.
     */
    public static class StopTimer {
        /**
         * The unique name of the timer to stop.
         */
        private final String timerName;

        /**
         * Constructs a {@code StopTimer} message.
         *
         * @param timerName The name of the timer to stop.
         */
        public StopTimer(String timerName) {
            this.timerName = timerName;
        }
    }

    /**
     * Internal message representing a timer tick for a specific timer.
     */
    private static class TimerTick {
        /**
         * The unique name of the timer that triggered the tick.
         */
        public final String timerName;

        /**
         * Constructs a {@code TimerTick} message.
         *
         * @param timerName The name of the timer that triggered the tick.
         */
        public TimerTick(String timerName) {
            this.timerName = timerName;
        }
    }

    /**
     * Factory method to create {@code Props} for this actor.
     *
     * @return A {@code Props} instance for creating a {@code UserRefreshTimerActor}.
     */
    public static Props props() {
        return Props.create(UserRefreshTimerActor.class);
    }

    /**
     * Handles the {@code StartTimer} message to start a periodic timer.
     *
     * <p>If the timer does not already exist, it schedules a periodic task that sends
     * {@code TimerTick} messages to this actor at the specified interval.
     *
     * @param msg The {@code StartTimer} message containing the timer name and interval.
     */
    private void handleStartTimer(StartTimer msg) {
        log.info("Starting timer: {} with interval: {} seconds", msg.timerName, msg.intervalSeconds);

        if (!lastRefreshTimes.containsKey(msg.timerName)) {
            lastRefreshTimes.put(msg.timerName, Instant.now());

            // Schedule the periodic timer
            getContext().system().scheduler().scheduleAtFixedRate(
                    Duration.create(0, TimeUnit.SECONDS),
                    Duration.create(msg.intervalSeconds, TimeUnit.SECONDS),
                    getSelf(),
                    new TimerTick(msg.timerName),
                    getContext().system().dispatcher(),
                    getSelf()
            );
        }
    }

    /**
     * Handles the {@code StopTimer} message to stop a timer.
     *
     * <p>The timer name is removed from the {@code lastRefreshTimes} map, effectively
     * ignoring future ticks from the timer.
     *
     * @param msg The {@code StopTimer} message containing the timer name.
     */
    private void handleStopTimer(StopTimer msg) {
        log.info("Stopping timer: {}", msg.timerName);
        lastRefreshTimes.remove(msg.timerName);
        // Note: The scheduled timer will continue running but will be ignored
        // when the timer name is not in lastRefreshTimes
    }

    /**
     * Handles the {@code TimerTick} message when a scheduled timer triggers a tick.
     *
     * <p>If the timer is active (exists in {@code lastRefreshTimes}), the actor updates
     * the last refresh time and notifies the parent actor about the tick event.
     *
     * @param tick The {@code TimerTick} message containing the timer name.
     */
    private void handleTimerTick(TimerTick tick) {
        if (lastRefreshTimes.containsKey(tick.timerName)) {
            log.debug("Timer tick for: {}", tick.timerName);
            lastRefreshTimes.put(tick.timerName, Instant.now());

            // Notify the parent actor (assumed to be CacheManagerActor) about the tick
            getContext().getParent().tell(
                    new UserActor.RefreshCache(),
                    getSelf()
            );
        }
    }

    /**
     * Defines the message handling behavior of this actor.
     *
     * <p>Handles:
     * <ul>
     *     <li>{@code StartTimer}: Starts a new timer with the specified interval.</li>
     *     <li>{@code StopTimer}: Stops an existing timer by its name.</li>
     *     <li>{@code TimerTick}: Processes a tick event for an active timer.</li>
     * </ul>
     *
     * @return The {@code Receive} instance defining the actor's behavior.
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(StartTimer.class, this::handleStartTimer)
                .match(StopTimer.class, this::handleStopTimer)
                .match(TimerTick.class, this::handleTimerTick)
                .build();
    }
}
