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
 * The {@code CacheRefreshTimerActor} is responsible for managing periodic timers
 * that trigger cache refresh operations. This actor schedules timers, tracks their
 * last execution times, and sends refresh notifications to the parent actor (assumed
 * to be the {@code CacheManagerActor}).
 *
 * <p>Timers are identified by unique names, and their intervals can be specified in seconds.
 * The actor can start multiple timers, stop timers, and handle periodic timer ticks.
 */
public class CacheRefreshTimerActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final Map<String, Instant> lastRefreshTimes = new HashMap<>();

    /**
     * Message to start a new timer.
     */
    public static class StartTimer {
        private final long intervalSeconds;
        private final String timerName;

        /**
         * Constructs a {@code StartTimer} message.
         *
         * @param timerName      The unique name of the timer.
         * @param intervalSeconds The interval in seconds for the timer.
         */
        public StartTimer(String timerName, long intervalSeconds) {
            this.timerName = timerName;
            this.intervalSeconds = intervalSeconds;
        }
    }

    /**
     * Message to stop an existing timer.
     */
    public static class StopTimer {
        private final String timerName;

        /**
         * Constructs a {@code StopTimer} message.
         *
         * @param timerName The unique name of the timer to stop.
         */
        public StopTimer(String timerName) {
            this.timerName = timerName;
        }
    }

    /**
     * Internal message to represent a timer tick event.
     */
    private static class TimerTick {
        public final String timerName;

        /**
         * Constructs a {@code TimerTick} message.
         *
         * @param timerName The unique name of the timer that triggered the tick.
         */
        public TimerTick(String timerName) {
            this.timerName = timerName;
        }
    }

    /**
     * Factory method to create {@code Props} for the actor.
     *
     * @return Props instance for creating a {@code CacheRefreshTimerActor}.
     */
    public static Props props() {
        return Props.create(CacheRefreshTimerActor.class);
    }

    /**
     * Handles the {@code StartTimer} message to start a new periodic timer.
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
     * Handles the {@code StopTimer} message to stop an existing timer.
     *
     * @param msg The {@code StopTimer} message containing the timer name to stop.
     */
    private void handleStopTimer(StopTimer msg) {
        log.info("Stopping timer: {}", msg.timerName);
        lastRefreshTimes.remove(msg.timerName);
        // Note: The scheduled timer will continue running but will be ignored
        // when the timer name is not in lastRefreshTimes
    }

    /**
     * Handles the {@code TimerTick} message to process a timer tick.
     *
     * @param tick The {@code TimerTick} message containing the timer name.
     */
    private void handleTimerTick(TimerTick tick) {
        if (lastRefreshTimes.containsKey(tick.timerName)) {
            log.debug("Timer tick for: {}", tick.timerName);
            lastRefreshTimes.put(tick.timerName, Instant.now());

            // Notify the parent actor (assumed to be CacheManagerActor) about the tick
            getContext().getParent().tell(
                    new CacheManagerActor.RefreshCache(),
                    getSelf()
            );
        }
    }

    /**
     * Defines the message handling behavior of this actor.
     *
     * @return The {@code Receive} instance defining how to handle messages.
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
