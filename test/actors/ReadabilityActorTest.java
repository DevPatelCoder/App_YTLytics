//import actors.ReadabilityActor;
//import akka.actor.ActorSystem;
//import akka.actor.Props;
//import akka.actor.Status;
//import akka.actor.testkit.javadsl.TestKit;
//import akka.testkit.javadsl.TestKit$;
//import models.api.YouTubeService;
//import models.data.Constants;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//
//import java.io.IOException;
//
//import static org.mockito.Mockito.*;
//import static org.junit.Assert.*;
//
//public class ReadabilityActorTest {
//
//    private ActorSystem system;
//    private YouTubeService mockYouTubeService;
//
//    @Before
//    public void setUp() {
//        system = ActorSystem.create();
//        mockYouTubeService = Mockito.mock(YouTubeService.class);
//    }
//
//    @After
//    public void tearDown() {
//        TestKit.shutdownActorSystem(system);
//    }
//
//    @Test
//    public void testHandleReadabilityAnalysis_Success() throws IOException {
//        // Arrange
//        String searchTerm = "Akka framework";
//        double expectedGrade = 8.5;
//        double expectedScore = 75.0;
//
//        // Mock the behavior of the YouTubeService
//        when(mockYouTubeService.getavgFleshGrade(searchTerm, Constants.MAX_DESC_SENTIMENT_COUNT)).thenReturn(expectedGrade);
//        when(mockYouTubeService.getavgFleshScore(searchTerm, Constants.MAX_DESC_SENTIMENT_COUNT)).thenReturn(expectedScore);
//
//        // Create the actor
//        TestKit probe = new TestKit(system);
//        Props props = ReadabilityActor.props(mockYouTubeService);
//        system.actorOf(props);
//
//        // Act
//        ReadabilityActor.AnalyzeReadabilityRequest request = new ReadabilityActor.AnalyzeReadabilityRequest(searchTerm);
//        probe.getRef().tell(request, probe.getRef());
//
//        // Assert
//        ReadabilityActor.AnalyzeReadabilityResponse response = probe.expectMsgClass(ReadabilityActor.AnalyzeReadabilityResponse.class);
//        assertEquals(expectedGrade, response.averageGrade, 0.1);
//        assertEquals(expectedScore, response.averageScore, 0.1);
//    }
//
//    @Test
//    public void testHandleReadabilityAnalysis_Failure() throws IOException {
//        // Arrange
//        String searchTerm = "Akka framework";
//        IOException exception = new IOException("Service failure");
//
//        // Mock the behavior of the YouTubeService to throw an exception
//        when(mockYouTubeService.getavgFleshGrade(searchTerm, Constants.MAX_DESC_SENTIMENT_COUNT)).thenThrow(exception);
//
//        // Create the actor
//        TestKit probe = new TestKit(system);
//        Props props = ReadabilityActor.props(mockYouTubeService);
//        system.actorOf(props);
//
//        // Act
//        ReadabilityActor.AnalyzeReadabilityRequest request = new ReadabilityActor.AnalyzeReadabilityRequest(searchTerm);
//        probe.getRef().tell(request, probe.getRef());
//
//        // Assert
//        probe.expectMsgClass(Status.Failure.class);
//        Status.Failure failure = probe.expectMsgClass(Status.Failure.class);
//        assertEquals(exception, failure.cause());
//    }
//
//    @Test
//    public void testHandleReadabilityAnalysis_EmptySearchTerm() throws IOException {
//        // Arrange
//        String searchTerm = "";
//        double expectedGrade = 0.0;
//        double expectedScore = 0.0;
//
//        // Mock the behavior of the YouTubeService
//        when(mockYouTubeService.getavgFleshGrade(searchTerm, Constants.MAX_DESC_SENTIMENT_COUNT)).thenReturn(expectedGrade);
//        when(mockYouTubeService.getavgFleshScore(searchTerm, Constants.MAX_DESC_SENTIMENT_COUNT)).thenReturn(expectedScore);
//
//        // Create the actor
//        TestKit probe = new TestKit(system);
//        Props props = ReadabilityActor.props(mockYouTubeService);
//        system.actorOf(props);
//
//        // Act
//        ReadabilityActor.AnalyzeReadabilityRequest request = new ReadabilityActor.AnalyzeReadabilityRequest(searchTerm);
//        probe.getRef().tell(request, probe.getRef());
//
//        // Assert
//        ReadabilityActor.AnalyzeReadabilityResponse response = probe.expectMsgClass(ReadabilityActor.AnalyzeReadabilityResponse.class);
//        assertEquals(expectedGrade, response.averageGrade, 0.1);
//        assertEquals(expectedScore, response.averageScore, 0.1);
//    }
//
//    @Test
//    public void testHandleReadabilityAnalysis_NullSearchTerm() throws IOException {
//        // Arrange
//        String searchTerm = null;
//        double expectedGrade = 0.0;
//        double expectedScore = 0.0;
//
//        // Mock the behavior of the YouTubeService
//        when(mockYouTubeService.getavgFleshGrade(searchTerm, Constants.MAX_DESC_SENTIMENT_COUNT)).thenReturn(expectedGrade);
//        when(mockYouTubeService.getavgFleshScore(searchTerm, Constants.MAX_DESC_SENTIMENT_COUNT)).thenReturn(expectedScore);
//
//        // Create the actor
//        TestKit probe = new TestKit(system);
//        Props props = ReadabilityActor.props(mockYouTubeService);
//        system.actorOf(props);
//
//        // Act
//        ReadabilityActor.AnalyzeReadabilityRequest request = new ReadabilityActor.AnalyzeReadabilityRequest(searchTerm);
//        probe.getRef().tell(request, probe.getRef());
//
//        // Assert
//        ReadabilityActor.AnalyzeReadabilityResponse response = probe.expectMsgClass(ReadabilityActor.AnalyzeReadabilityResponse.class);
//        assertEquals(expectedGrade, response.averageGrade, 0.1);
//        assertEquals(expectedScore, response.averageScore, 0.1);
//    }
//
//    @Test
//    public void testHandleReadabilityAnalysis_BoundaryValues() throws IOException {
//        // Arrange
//        String searchTerm = "boundary test";
//        double expectedGrade = 10.0;  // Assuming boundary returns max grade
//        double expectedScore = 90.0;  // Assuming boundary returns max score
//
//        // Mock the behavior of the YouTubeService
//        when(mockYouTubeService.getavgFleshGrade(searchTerm, Constants.MAX_DESC_SENTIMENT_COUNT)).thenReturn(expectedGrade);
//        when(mockYouTubeService.getavgFleshScore(searchTerm, Constants.MAX_DESC_SENTIMENT_COUNT)).thenReturn(expectedScore);
//
//        // Create the actor
//        TestKit$ probe = new TestKit(system);
//        Props props = ReadabilityActor.props(mockYouTubeService);
//        system.actorOf(props);
//
//        // Act
//        ReadabilityActor.AnalyzeReadabilityRequest request = new ReadabilityActor.AnalyzeReadabilityRequest(searchTerm);
//        probe.getRef().tell(request, probe.getRef());
//
//        // Assert
//        ReadabilityActor.AnalyzeReadabilityResponse response = probe.expectMsgClass(ReadabilityActor.AnalyzeReadabilityResponse.class);
//        assertEquals(expectedGrade, response.averageGrade, 0.1);
//        assertEquals(expectedScore, response.averageScore, 0.1);
//    }
//}
