import com.google.inject.AbstractModule;
import models.api.CacheManager;


/**
 * A module that configures the cache manager for dependency injection using Google Guice.
 * <p>
 * This module binds the {@link CacheManager} class to the Guice injector and ensures that it is 
 * initialized as an eager singleton. It uses the Play Framework's environment and configuration
 * to provide necessary dependencies for the {@link CacheManager}.
 * </p>
 *
 */
public class CacheModule extends AbstractModule {
    private final play.api.Environment environment;
    private final play.api.Configuration configuration;

    /**
     * Constructor to initialize the CacheModule with the environment and configuration.
     *
     * @param environment the Play Framework environment object
     * @param configuration the Play Framework configuration object
     */
    public CacheModule(play.api.Environment environment, play.api.Configuration configuration) {
        this.environment = environment;
        this.configuration = configuration;
    }

    /**
     * Configures the binding for the CacheManager class.
     * <p>
     * This method binds the {@link CacheManager} class to the Guice injector as an eager singleton.
     * The singleton will be created immediately upon application startup.
     * </p>
     */
    @Override
    protected void configure() {
        bind(CacheManager.class).asEagerSingleton();
    }
}
