package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.DeciderBuilder;

import java.time.Duration;

/**
 * The {@code SearchSupervisorActor} supervises the lifecycle of its child actors and manages error recovery strategies.
 *
 * <p>It implements a supervision strategy to handle exceptions in its child actors, deciding whether to resume,
 * restart, stop, or escalate failures. It also creates and manages child actors such as {@code UserActor}.
 */
public class SearchSupervisorActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private ActorRef cacheManagerActor;
    private ActorRef userActor;

    /**
     * Constructs a {@code SearchSupervisorActor}.
     *
     * @param cacheManagerActor The reference to the {@code CacheManagerActor} for managing cached data.
     */
    public SearchSupervisorActor(ActorRef cacheManagerActor) {
        this.cacheManagerActor = cacheManagerActor;
    }

    /**
     * Factory method to create {@code Props} for this actor.
     *
     * @param cacheManagerActor The reference to the {@code CacheManagerActor}.
     * @return A {@code Props} instance for creating this actor.
     */
    public static Props props(ActorRef cacheManagerActor) {
        return Props.create(SearchSupervisorActor.class, cacheManagerActor);
    }

    /**
     * Defines the supervision strategy for handling child actor failures.
     *
     * <p>The strategy is as follows:
     * <ul>
     *     <li>Stop the child actor on {@code IllegalArgumentException}.</li>
     *     <li>Restart the child actor on {@code NullPointerException}.</li>
     *     <li>Resume the child actor for most other exceptions.</li>
     *     <li>Escalate unhandled cases to the parent actor.</li>
     * </ul>
     *
     * @return The configured {@code SupervisorStrategy}.
     */
    @Override
    public SupervisorStrategy supervisorStrategy() {
        return new OneForOneStrategy(
                10, // Maximum number of restart attempts
                Duration.ofMinutes(1), // Reset window for restart count
                DeciderBuilder
                        .match(Exception.class, e -> {
                            // Log the error
                            log.error(e, "Error in child actor: {}", e.getMessage());

                            // Different strategies based on exception type
                            if (e instanceof IllegalArgumentException) {
                                // For invalid arguments, stop the actor
                                return SupervisorStrategy.stop();
                            } else if (e instanceof NullPointerException) {
                                // Restart on null pointer
                                return SupervisorStrategy.restart();
                            } else {
                                // For most other exceptions, resume
                                return SupervisorStrategy.resume();
                            }
                        })
                        .matchAny(msg -> SupervisorStrategy.escalate())
                        .build()
        );
    }

    /**
     * Defines the message handling behavior of this actor.
     *
     * <p>Handles:
     * <ul>
     *     <li>{@code CreateChildActors}: Creates a {@code UserActor} under supervision.</li>
     *     <li>{@code RestartUserActor}: Restarts the {@code UserActor} programmatically if needed.</li>
     * </ul>
     *
     * @return The {@code Receive} instance defining the actor's behavior.
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CreateChildActors.class, msg -> {
                    // Create child actors under supervision
                    userActor = getContext().actorOf(
                            UserActor.props(msg.webSocketOut, cacheManagerActor),
                            "supervised-user-actor"
                    );
                })
                .match(RestartUserActor.class, msg -> {
                    // Method to programmatically restart user actor if needed
                    if (userActor != null) {
                        getContext().stop(userActor);
                        userActor = getContext().actorOf(
                                UserActor.props(msg.webSocketOut, cacheManagerActor),
                                "restarted-user-actor"
                        );
                    }
                })
                .build();
    }

    // Message classes for communication
    /**
     * Message to request the creation of child actors.
     */
    public static class CreateChildActors {
        /**
         * The {@code ActorRef} for the WebSocket connection.
         */
        public final ActorRef webSocketOut;

        /**
         * Constructs a {@code CreateChildActors} message.
         *
         * @param webSocketOut The WebSocket output actor reference.
         */
        public CreateChildActors(ActorRef webSocketOut) {
            this.webSocketOut = webSocketOut;
        }
    }

    /**
     * Message to request a restart of the {@code UserActor}.
     */
    public static class RestartUserActor {
        /**
         * The {@code ActorRef} for the WebSocket connection.
         */
        public final ActorRef webSocketOut;

        /**
         * Constructs a {@code RestartUserActor} message.
         *
         * @param webSocketOut The WebSocket output actor reference.
         */
        public RestartUserActor(ActorRef webSocketOut) {
            this.webSocketOut = webSocketOut;
        }
    }
}