package controllers;

import actors.CacheManagerActor;
import actors.UserActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Status;
import akka.pattern.Patterns;
import akka.stream.Materializer;

import actors.CacheManagerActor;
import actors.UserActor;
import actors.*;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Status;
import akka.pattern.Patterns;
import akka.stream.Materializer;
import models.api.CacheManager;
import models.api.YouTubeService;
import models.data.ChannelVideoData;
import models.data.VideoData;
import models.data.WordStatData;
import play.libs.streams.ActorFlow;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.WebSocket;
import scala.concurrent.Future;
import views.html.channelProfile;
import views.html.index;

import scala.concurrent.ExecutionContext;

import javax.inject.Inject;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;

import static scala.compat.java8.FutureConverters.toJava;

/**
 * Controller class responsible for handling HTTP requests and rendering views.
 */
public class HomeController extends Controller {

    private final CacheManager cacheManager;
    private final ActorSystem actorSystem;
    private final ActorRef cacheManagerActor;
    private final Materializer materializer;

    private final ConcurrentHashMap<String, ActorRef> sessionActorMap = new ConcurrentHashMap<>();

    /**
     * Constructor for HomeController.
     * @param cacheManager An instance of CacheManager for data retrieval and caching.
     */
    @Inject
    public HomeController(com.typesafe.config.Config config, CacheManager cacheManager, ActorSystem actorSystem, Materializer materializer) throws GeneralSecurityException, IOException {
        this.actorSystem = actorSystem;
        this.cacheManager = cacheManager;
        this.cacheManagerActor = actorSystem.actorOf(CacheManagerActor.props(config));
        this.materializer = materializer;

    }

    private final String SESSION_KEY = "searchedTerm";

    /**
     * Renders the index page with search results.
     * @param request The HTTP request.
     * @return CompletionStage<Result> rendering the index page.
     */

        public WebSocket searchWebSocket () {
            ActorRef supervisorActor = actorSystem.actorOf(
                    SearchSupervisorActor.props(cacheManagerActor),
                    "search-supervisor"
            );

            return WebSocket.Text.accept(request ->
                    ActorFlow.actorRef(out -> {
                        // Send message to supervisor to create user actor
                        supervisorActor.tell(
                                new SearchSupervisorActor.CreateChildActors(out),
                                ActorRef.noSender()
                        );
                        return UserActor.props(out, cacheManagerActor);
                    }, actorSystem, materializer)
            );
        }


        public CompletionStage<Result> index (Http.Request request){
            return CompletableFuture.completedFuture(
                    ok(index.render(Collections.emptyList(), request))
            );
        }


        /**
         * Displays the profile of a channel with its details and last 10 videos.
         * @param channelId The ID of the YouTube channel.
         * @param request The HTTP request.
         * @return CompletionStage<Result> rendering the channel profile.
         */
        public CompletionStage<Result> channelProfile(String channelId, Http.Request request) {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    // Fetch channel results
                    ChannelVideoData data = cacheManager.getChannelResults(channelId);

                    if (data == null) {
                        return internalServerError("Channel data not found.");
                    }

                    // Ensure we can safely call getChannelData() and getVideoDataList()
                    return ok(channelProfile.render(data.getChannelData(), data.getVideoDataList()));
                } catch (IOException e) {
                    // Handle IOException and return an internal server error
                    return internalServerError("An error occurred while retrieving channel data.");
                } catch (Exception e) {
                    // Catch other unexpected exceptions
                    return internalServerError("An unexpected error occurred: " + e.getMessage());
                }
            });
        }



    /**
         * Clears the search history from the session.
         * @param request The HTTP request.
         * @return CompletionStage<Result> redirecting to the index page after clearing session data.
         */
        public CompletionStage<Result> clearHistory (Http.Request request){
            return CompletableFuture.completedFuture(redirect("/").removingFromSession(request, SESSION_KEY));
        }

        /**
         * Retrieves word statistics for a given query.
         * @param query The search query for word statistics.
         * @return CompletionStage<Result> rendering the word statistics view.
         */
        public CompletionStage<Result> getWordStats (String query){
            return CompletableFuture.supplyAsync(() -> {
                try {
                    WordStatData data = cacheManager.getWordStats(query);
                    return ok(views.html.wordStats.render(data));
                } catch (IOException e) {
                    throw new CompletionException(e);
                }
            });
        }

        /**
         * Displays video tags for a given video.
         * @param videoId The ID of the video.
         * @return CompletionStage<Result> rendering the tags view.
         */
        public CompletionStage<Result> showTags (String videoId){
            return CompletableFuture.supplyAsync(() -> {
                try {
                    List<String> tags = cacheManager.getVideoTags(videoId);
                    return ok(views.html.tags.render(videoId, tags));
                } catch (IOException e) {
                    return internalServerError("Error fetching tags: " + e.getMessage());
                }
            });
        }

        /**
         * Displays videos by a specific tag.
         * @param tag The tag used for video search.
         * @return CompletionStage<Result> rendering a list of videos by the tag.
         */
        public CompletionStage<Result> showVideosByTag (String tag){
            final YouTubeService youtubeService;
            try {
                youtubeService = new YouTubeService("AIzaSyBlixVtFM2zVl8Te-hMJp1wgQFJaO8z2YE");
            } catch (GeneralSecurityException e) {
                return CompletableFuture.completedFuture(internalServerError("Security error while initializing YouTube service: " + e.getMessage()));
            } catch (IOException e) {
                return CompletableFuture.completedFuture(internalServerError("Error initializing YouTube service: " + e.getMessage()));
            }

            return CompletableFuture.supplyAsync(() -> {
                try {
                    List<VideoData> videos = youtubeService.searchVideos(tag, 10);
                    return ok(views.html.videoList.render(tag, videos));
                } catch (IOException e) {
                    return internalServerError("Error fetching videos: " + e.getMessage());
                }
            });
        }

}
