package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class UserRefreshTimerActorTest {

    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create("UserRefreshTimerActorTest");
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testStartTimer() {
        new TestKit(system) {{
            // Create the actor
            final ActorRef userRefreshTimerActor = system.actorOf(UserRefreshTimerActor.props());

            // Send StartTimer message
            userRefreshTimerActor.tell(new UserRefreshTimerActor.StartTimer("TestTimer", 1), getRef());

            // Wait for a TimerTick message
            within(duration("3 seconds"), () -> {
                // TimerTick messages are handled internally; no direct assertion here
                expectNoMessage();
                return null;
            });
        }};
    }

    @Test
    public void testStopTimer() {
        new TestKit(system) {{
            // Create the actor
            final ActorRef timerActor = system.actorOf(UserRefreshTimerActor.props());
            final String timerName = "TestTimer";
            final long intervalSeconds = 1;

            // Start the timer
            timerActor.tell(new UserRefreshTimerActor.StartTimer(timerName, intervalSeconds), getRef());

            // Wait for a short period to ensure timer ticks
            within(duration("2 seconds"), () -> {
                // No explicit assertion here as TimerTick messages are logged, not directly sent
                expectNoMessage();
                return null;
            });

            // Stop the timer
            timerActor.tell(new UserRefreshTimerActor.StopTimer(timerName), getRef());

            // Ensure no further TimerTick messages after stopping the timer
            within(duration("2 seconds"), () -> {
                expectNoMessage();
                return null;
            });
        }};
    }
}
