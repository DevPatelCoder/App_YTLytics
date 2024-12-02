package actors;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.compat.java8.FutureConverters;

import static org.junit.Assert.assertNotNull;

public class SearchSupervisorActorTest {

    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testCreateChildActors() {
        new TestKit(system) {{
            ActorRef cacheManager = getTestActor(); // Mocked cache manager actor
            ActorRef supervisor = system.actorOf(SearchSupervisorActor.props(cacheManager));

            // Create child actor
            ActorRef webSocketOut = getTestActor(); // Mocked WebSocket actor
            supervisor.tell(new SearchSupervisorActor.CreateChildActors(webSocketOut), getRef());

            // Verify child actor was created
            within(duration("3 seconds"), () -> {
                expectNoMessage(); // No response expected from supervisor
                ActorRef child = FutureConverters.toJava(
                        system.actorSelection(supervisor.path().child("supervised-user-actor"))
                                .resolveOne(duration("1 second"))
                ).toCompletableFuture().join();
                assertNotNull(child);
                return null;
            });
        }};
    }

    @Test
    public void testRestartUserActor() {
        new TestKit(system) {{
            ActorRef cacheManager = getTestActor(); // Mocked cache manager actor
            ActorRef supervisor = system.actorOf(SearchSupervisorActor.props(cacheManager));

            // Create and restart the child actor
            ActorRef webSocketOut = getTestActor(); // Mocked WebSocket actor
            supervisor.tell(new SearchSupervisorActor.CreateChildActors(webSocketOut), getRef());
            supervisor.tell(new SearchSupervisorActor.RestartUserActor(webSocketOut), getRef());

            // Verify child actor was restarted
            within(duration("3 seconds"), () -> {
                expectNoMessage(); // No response expected from supervisor
                ActorRef restartedChild = FutureConverters.toJava(
                        system.actorSelection(supervisor.path().child("restarted-user-actor"))
                                .resolveOne(duration("1 second"))
                ).toCompletableFuture().join();
                assertNotNull(restartedChild);
                return null;
            });
        }};
    }

}
