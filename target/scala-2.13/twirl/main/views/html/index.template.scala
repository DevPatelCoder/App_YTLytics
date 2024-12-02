
package views.html

import _root_.play.twirl.api.TwirlFeatureImports._
import _root_.play.twirl.api.TwirlHelperImports._
import _root_.play.twirl.api.Html
import _root_.play.twirl.api.JavaScript
import _root_.play.twirl.api.Txt
import _root_.play.twirl.api.Xml
import models._
import controllers._
import play.api.i18n._
import views.html._
import play.api.templates.PlayMagic._
import java.lang._
import java.util._
import play.core.j.PlayMagicForJava._
import play.mvc._
import play.api.data.Field
import play.data._
import play.core.j.PlayFormsMagicForJava._
import scala.jdk.CollectionConverters._
/*1.2*/import models.data.VideoSearchData
/*2.2*/import play.mvc.Http.RequestHeader

object index extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template2[List[VideoSearchData],RequestHeader,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*3.2*/(searchHistory: List[VideoSearchData])(implicit request: RequestHeader):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*3.73*/("""

"""),format.raw/*5.1*/("""<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>YouTube Search Results</title>
        <link rel="stylesheet" href=""""),_display_(/*11.39*/routes/*11.45*/.Assets.versioned("stylesheets/main.css")),format.raw/*11.86*/("""">
        <style>
            body """),format.raw/*13.18*/("""{"""),format.raw/*13.19*/("""
                """),format.raw/*14.17*/("""font-family: Arial, sans-serif;
                margin: 0;
                padding: 0;
                background-color: #fff; /* White background for the theme */
                color: #333;
            """),format.raw/*19.13*/("""}"""),format.raw/*19.14*/("""
            """),format.raw/*20.13*/(""".nav-bar """),format.raw/*20.22*/("""{"""),format.raw/*20.23*/("""
                """),format.raw/*21.17*/("""display: flex;
                align-items: center;
                background-color: #d32f2f; /* Red color for the nav-bar */
                padding: 10px 20px;
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            """),format.raw/*26.13*/("""}"""),format.raw/*26.14*/("""
            """),format.raw/*27.13*/(""".nav-bar h1 """),format.raw/*27.25*/("""{"""),format.raw/*27.26*/("""
                """),format.raw/*28.17*/("""margin: 0;
                color: #fff; /* White text for contrast */
            """),format.raw/*30.13*/("""}"""),format.raw/*30.14*/("""
            """),format.raw/*31.13*/(""".search-container """),format.raw/*31.31*/("""{"""),format.raw/*31.32*/("""
                """),format.raw/*32.17*/("""display: flex;
                justify-content: center;
                align-items: center;
                padding: 20px;
            """),format.raw/*36.13*/("""}"""),format.raw/*36.14*/("""
            """),format.raw/*37.13*/(""".search-container form """),format.raw/*37.36*/("""{"""),format.raw/*37.37*/("""
                """),format.raw/*38.17*/("""display: flex;
                flex-direction: column;
                align-items: center;
                width: 100%;
                max-width: 400px;
            """),format.raw/*43.13*/("""}"""),format.raw/*43.14*/("""
            """),format.raw/*44.13*/(""".search-container input """),format.raw/*44.37*/("""{"""),format.raw/*44.38*/("""
                """),format.raw/*45.17*/("""padding: 8px 12px;
                font-size: 14px;
                border-radius: 25px;
                border: 1px solid #d32f2f; /* Red border */
                width: 100%;
                box-sizing: border-box;
                margin-bottom: 10px;
            """),format.raw/*52.13*/("""}"""),format.raw/*52.14*/("""
            """),format.raw/*53.13*/(""".search-container input:focus """),format.raw/*53.43*/("""{"""),format.raw/*53.44*/("""
                """),format.raw/*54.17*/("""outline: none;
                border-color: #b71c1c; /* Darker red on focus */
            """),format.raw/*56.13*/("""}"""),format.raw/*56.14*/("""
            """),format.raw/*57.13*/(""".search-container button """),format.raw/*57.38*/("""{"""),format.raw/*57.39*/("""
                """),format.raw/*58.17*/("""padding: 8px 12px;
                font-size: 14px;
                border-radius: 25px;
                border: 1px solid #d32f2f;
                background-color: #d32f2f; /* Red background */
                color: white;
                cursor: pointer;
            """),format.raw/*65.13*/("""}"""),format.raw/*65.14*/("""
            """),format.raw/*66.13*/(""".search-container button:hover """),format.raw/*66.44*/("""{"""),format.raw/*66.45*/("""
                """),format.raw/*67.17*/("""background-color: #b71c1c; /* Darker red on hover */
            """),format.raw/*68.13*/("""}"""),format.raw/*68.14*/("""
            """),format.raw/*69.13*/(""".history-container """),format.raw/*69.32*/("""{"""),format.raw/*69.33*/("""
                """),format.raw/*70.17*/("""max-width: 800px;
                margin: 0 auto;
                padding: 20px;
            """),format.raw/*73.13*/("""}"""),format.raw/*73.14*/("""
            """),format.raw/*74.13*/(""".search-history-list """),format.raw/*74.34*/("""{"""),format.raw/*74.35*/("""
                """),format.raw/*75.17*/("""list-style-type: none;
                padding: 0;
            """),format.raw/*77.13*/("""}"""),format.raw/*77.14*/("""
            """),format.raw/*78.13*/(""".search-history-list > li """),format.raw/*78.39*/("""{"""),format.raw/*78.40*/("""
                """),format.raw/*79.17*/("""background-color: #fff; /* White background for items */
                border: 1px solid #d32f2f; /* Red border for emphasis */
                border-radius: 8px;
                padding: 15px;
                margin-bottom: 15px;
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            """),format.raw/*85.13*/("""}"""),format.raw/*85.14*/("""
            """),format.raw/*86.13*/(""".video-list """),format.raw/*86.25*/("""{"""),format.raw/*86.26*/("""
                """),format.raw/*87.17*/("""list-style-type: none;
                padding: 0;
            """),format.raw/*89.13*/("""}"""),format.raw/*89.14*/("""
            """),format.raw/*90.13*/(""".video-card """),format.raw/*90.25*/("""{"""),format.raw/*90.26*/("""
                """),format.raw/*91.17*/("""display: flex;
                margin-bottom: 15px;
                background-color: #fff; /* White background for video cards */
                border: 1px solid #d32f2f; /* Red border */
                border-radius: 8px;
                overflow: hidden;
            """),format.raw/*97.13*/("""}"""),format.raw/*97.14*/("""
            """),format.raw/*98.13*/(""".video-card img """),format.raw/*98.29*/("""{"""),format.raw/*98.30*/("""
                """),format.raw/*99.17*/("""width: 200px;
                height: 150px;
                object-fit: cover;
            """),format.raw/*102.13*/("""}"""),format.raw/*102.14*/("""
            """),format.raw/*103.13*/(""".video-info """),format.raw/*103.25*/("""{"""),format.raw/*103.26*/("""
                """),format.raw/*104.17*/("""padding: 10px;
                flex-grow: 1;
            """),format.raw/*106.13*/("""}"""),format.raw/*106.14*/("""
            """),format.raw/*107.13*/(""".video-info a """),format.raw/*107.27*/("""{"""),format.raw/*107.28*/("""
                """),format.raw/*108.17*/("""color: #d32f2f; /* Red for links */
                text-decoration: none;
            """),format.raw/*110.13*/("""}"""),format.raw/*110.14*/("""
            """),format.raw/*111.13*/(""".video-info a:hover """),format.raw/*111.33*/("""{"""),format.raw/*111.34*/("""
                """),format.raw/*112.17*/("""text-decoration: underline;
            """),format.raw/*113.13*/("""}"""),format.raw/*113.14*/("""
            """),format.raw/*114.13*/("""#loading-indicator """),format.raw/*114.32*/("""{"""),format.raw/*114.33*/("""
                """),format.raw/*115.17*/("""color: #d32f2f; /* Red text for loading indicator */
                display: none;
            """),format.raw/*117.13*/("""}"""),format.raw/*117.14*/("""
            """),format.raw/*118.13*/("""#error-container """),format.raw/*118.30*/("""{"""),format.raw/*118.31*/("""
                """),format.raw/*119.17*/("""color: #b71c1c; /* Darker red for errors */
            """),format.raw/*120.13*/("""}"""),format.raw/*120.14*/("""
        """),format.raw/*121.9*/("""</style>

    </head>
    <body>
            <!-- Navigation Bar -->
        <div class="nav-bar">
            <h1>Welcome To YTLytics</h1>
        </div>

            <!-- Search Container -->
        <div class="search-container">
            <form id="searchForm">
                <input type="text" id="searchQuery" placeholder="Search Query..." required>
                <button type="submit">Search</button>
            </form>
        </div>

            <!-- Loading and Error Indicators -->
        <div id="loading-indicator">
            Searching... Please wait for a reasonable time
        </div>
        <div id="error-container"></div>

            <!-- Results Container -->
        <div class="history-container" id="results-container">
            """),format.raw/*146.51*/("""
            """),_display_(/*147.14*/for(history <- searchHistory) yield /*147.43*/ {_display_(Seq[Any](format.raw/*147.45*/("""
                """),_display_(if(history != null && history.getVideos() != null && !history.getVideos().isEmpty())/*148.102*/ {_display_(Seq[Any](format.raw/*148.104*/("""
                    """),format.raw/*149.21*/("""<div class="result-item">
                        <h3>Query: """),_display_(/*150.37*/history/*150.44*/.getQuery()),format.raw/*150.55*/("""</h3>
                        <p>Sentiment: """),_display_(/*151.40*/history/*151.47*/.getSentiment()),format.raw/*151.62*/("""</p>
                        <p>FleshKincidan Grade: """),_display_(/*152.50*/history/*152.57*/.getAvgFleshGrad()),format.raw/*152.75*/("""</p>
                        <p>Flesh Read Ease Score: """),_display_(/*153.52*/history/*153.59*/.getAvgFleshScore()),format.raw/*153.78*/("""</p>
                        """),_display_(/*154.26*/for(video <- history.getVideos()) yield /*154.59*/ {_display_(Seq[Any](format.raw/*154.61*/("""
                            """),format.raw/*155.29*/("""<div class="video-item">
                                <h4>"""),_display_(/*156.38*/video/*156.43*/.getTitle()),format.raw/*156.54*/("""</h4>
                                <p>Channel: """),_display_(/*157.46*/video/*157.51*/.getChannelTitle()),format.raw/*157.69*/("""</p>
                            </div>
                        """)))}),format.raw/*159.26*/("""
                    """),format.raw/*160.21*/("""</div>
                """)))} else {null} ),format.raw/*161.18*/("""
            """)))}),format.raw/*162.14*/("""
        """),format.raw/*163.9*/("""</div>

        <script>
                document.addEventListener('DOMContentLoaded', () => """),format.raw/*166.69*/("""{"""),format.raw/*166.70*/("""
                    """),format.raw/*167.21*/("""const searchForm = document.getElementById('searchForm');
                    const searchQuery = document.getElementById('searchQuery');
                    const resultsContainer = document.getElementById('results-container');
                    const loadingIndicator = document.getElementById('loading-indicator');
                    const errorContainer = document.getElementById('error-container');
                    let socket = null;

                    // WebSocket URL from routes
                    const wsUrl = """"),_display_(/*175.37*/routes/*175.43*/.HomeController.searchWebSocket().webSocketURL()),format.raw/*175.91*/("""";

                    function initWebSocket() """),format.raw/*177.46*/("""{"""),format.raw/*177.47*/("""
                        """),format.raw/*178.25*/("""// Create WebSocket connection
                        socket = new WebSocket(wsUrl);

                        socket.onopen = () => """),format.raw/*181.47*/("""{"""),format.raw/*181.48*/("""
                            """),format.raw/*182.29*/("""console.log('WebSocket connection established');
                            errorContainer.textContent = ''; // Clear any previous errors
                        """),format.raw/*184.25*/("""}"""),format.raw/*184.26*/(""";

                        socket.onmessage = (event) => """),format.raw/*186.55*/("""{"""),format.raw/*186.56*/("""
                            """),format.raw/*187.29*/("""try """),format.raw/*187.33*/("""{"""),format.raw/*187.34*/("""
                                """),format.raw/*188.33*/("""const data = JSON.parse(event.data);
                                displaySearchResults(data);
                            """),format.raw/*190.29*/("""}"""),format.raw/*190.30*/(""" """),format.raw/*190.31*/("""catch (error) """),format.raw/*190.45*/("""{"""),format.raw/*190.46*/("""
                                """),format.raw/*191.33*/("""console.error('Error parsing message:', error);
                                errorContainer.textContent = 'Error processing search results';
                            """),format.raw/*193.29*/("""}"""),format.raw/*193.30*/(""" """),format.raw/*193.31*/("""finally """),format.raw/*193.39*/("""{"""),format.raw/*193.40*/("""
                                """),format.raw/*194.33*/("""loadingIndicator.style.display = 'none';
                            """),format.raw/*195.29*/("""}"""),format.raw/*195.30*/("""
                        """),format.raw/*196.25*/("""}"""),format.raw/*196.26*/(""";

                        socket.onclose = (event) => """),format.raw/*198.53*/("""{"""),format.raw/*198.54*/("""
                            """),format.raw/*199.29*/("""console.log('WebSocket connection closed');
                            errorContainer.textContent = 'WebSocket disconnected. Reconnecting...';
                            // Attempt to reconnect
                            setTimeout(initWebSocket, 3000);
                        """),format.raw/*203.25*/("""}"""),format.raw/*203.26*/(""";

                        socket.onerror = (error) => """),format.raw/*205.53*/("""{"""),format.raw/*205.54*/("""
                            """),format.raw/*206.29*/("""console.error('WebSocket error:', error);
                            errorContainer.textContent = 'WebSocket connection error';
                            loadingIndicator.style.display = 'none';
                        """),format.raw/*209.25*/("""}"""),format.raw/*209.26*/(""";
                    """),format.raw/*210.21*/("""}"""),format.raw/*210.22*/("""

                    """),format.raw/*212.21*/("""let searchResultsCount = 0;
                    const MAX_SEARCH_RESULTS = 10;

                    function displaySearchResults(searchData) """),format.raw/*215.63*/("""{"""),format.raw/*215.64*/("""
                        """),format.raw/*216.25*/("""// Check if search data is valid
                        if (!searchData || !searchData.videos || searchData.videos.length === 0) """),format.raw/*217.98*/("""{"""),format.raw/*217.99*/("""
                            """),format.raw/*218.29*/("""resultsContainer.innerHTML += '<p>No results found</p>';
                            return;
                        """),format.raw/*220.25*/("""}"""),format.raw/*220.26*/("""

                        """),format.raw/*222.25*/("""// Remove existing result with the same query if present
                        const existingResults = resultsContainer.querySelectorAll('.result-item');
                        existingResults.forEach(item => """),format.raw/*224.57*/("""{"""),format.raw/*224.58*/("""
                            """),format.raw/*225.29*/("""const queryHeader = item.querySelector('h3');
                            if (queryHeader && queryHeader.textContent === `Query: $"""),format.raw/*226.85*/("""{"""),format.raw/*226.86*/("""searchData.query"""),format.raw/*226.102*/("""}"""),format.raw/*226.103*/("""`) """),format.raw/*226.106*/("""{"""),format.raw/*226.107*/("""
                                """),format.raw/*227.33*/("""resultsContainer.removeChild(item);
                                searchResultsCount--; // Decrement count as we're removing an item
                            """),format.raw/*229.29*/("""}"""),format.raw/*229.30*/("""
                        """),format.raw/*230.25*/("""}"""),format.raw/*230.26*/(""");

                        // Create result item
                        const resultItem = document.createElement('div');
                        resultItem.classList.add('result-item');

                        // Add query and sentiment
                        const queryHeader = document.createElement('h3');
                        queryHeader.textContent = `Query: $"""),format.raw/*238.60*/("""{"""),format.raw/*238.61*/("""searchData.query"""),format.raw/*238.77*/("""}"""),format.raw/*238.78*/("""`;
                        resultItem.appendChild(queryHeader);

                        const sentimentPara = document.createElement('p');
                        sentimentPara.textContent = `Sentiment: $"""),format.raw/*242.66*/("""{"""),format.raw/*242.67*/("""searchData.sentiment || 'N/A'"""),format.raw/*242.96*/("""}"""),format.raw/*242.97*/("""`;
                        resultItem.appendChild(sentimentPara);

                        // Add Flesch-Kincaid and Flesch Reading Ease scores
                        if (searchData.fleschKincaidGrade !== undefined) """),format.raw/*246.74*/("""{"""),format.raw/*246.75*/("""
                            """),format.raw/*247.29*/("""const fleschGradePara = document.createElement('p');
                            fleschGradePara.textContent = `Flesch-Kincaid Grade: $"""),format.raw/*248.83*/("""{"""),format.raw/*248.84*/("""searchData.fleschKincaidGrade.toFixed(2)"""),format.raw/*248.124*/("""}"""),format.raw/*248.125*/("""`;
                            resultItem.appendChild(fleschGradePara);
                        """),format.raw/*250.25*/("""}"""),format.raw/*250.26*/("""

                        """),format.raw/*252.25*/("""if (searchData.fleschReadingEase !== undefined) """),format.raw/*252.73*/("""{"""),format.raw/*252.74*/("""
                            """),format.raw/*253.29*/("""const fleschEasePara = document.createElement('p');
                            fleschEasePara.textContent = `Flesch Reading Ease: $"""),format.raw/*254.81*/("""{"""),format.raw/*254.82*/("""searchData.fleschReadingEase.toFixed(2)"""),format.raw/*254.121*/("""}"""),format.raw/*254.122*/("""`;
                            resultItem.appendChild(fleschEasePara);
                        """),format.raw/*256.25*/("""}"""),format.raw/*256.26*/("""


                        """),format.raw/*259.25*/("""// Add videos
                        const videoList = document.createElement('ul');
                        videoList.classList.add('video-list');

                        searchData.videos.forEach(video => """),format.raw/*263.60*/("""{"""),format.raw/*263.61*/("""
                            """),format.raw/*264.29*/("""const videoItem = document.createElement('li');
                            videoItem.classList.add('video-card');

                            // Video thumbnail
                            const thumbnailImg = document.createElement('img');
                            thumbnailImg.src = video.thumbnailUrl || '/placeholder-thumbnail.jpg';
                            thumbnailImg.alt = 'Video Thumbnail';

                            // Video info container
                            const videoInfo = document.createElement('div');
                            videoInfo.classList.add('video-info');

                            const videoTitle = document.createElement('h4');
                            const videoTitleLink = document.createElement('a');
                            videoTitleLink.href = video.videoUrl || '#';
                            videoTitleLink.target = '_blank';
                            videoTitleLink.textContent = video.title;
                            videoTitle.appendChild(videoTitleLink);

                            const channelPara = document.createElement('p');
                            const channelLink = document.createElement('a');
                            channelLink.href = `/channel/$"""),format.raw/*285.59*/("""{"""),format.raw/*285.60*/("""video.channelId"""),format.raw/*285.75*/("""}"""),format.raw/*285.76*/("""`;
                            channelLink.target = '_blank';
                            channelLink.textContent = `Channel: $"""),format.raw/*287.66*/("""{"""),format.raw/*287.67*/("""video.channelTitle"""),format.raw/*287.85*/("""}"""),format.raw/*287.86*/("""`;
                            channelPara.appendChild(channelLink);

                            const descriptionPara = document.createElement('p');
                            descriptionPara.textContent = `Description: $"""),format.raw/*291.74*/("""{"""),format.raw/*291.75*/("""video.description || 'No description'"""),format.raw/*291.112*/("""}"""),format.raw/*291.113*/("""`;

                            const tagLink = document.createElement('p');
                            const tagsAnchor = document.createElement('a');
                            tagsAnchor.href = `/tags/$"""),format.raw/*295.55*/("""{"""),format.raw/*295.56*/("""video.videoId"""),format.raw/*295.69*/("""}"""),format.raw/*295.70*/("""`;
                            tagsAnchor.textContent = 'Tags';
                            tagLink.appendChild(tagsAnchor);

                            videoInfo.appendChild(videoTitle);
                            videoInfo.appendChild(channelPara);
                            videoInfo.appendChild(descriptionPara);
                            videoInfo.appendChild(tagLink);

                            videoItem.appendChild(thumbnailImg);
                            videoItem.appendChild(videoInfo);

                            videoList.appendChild(videoItem);
                        """),format.raw/*308.25*/("""}"""),format.raw/*308.26*/(""");

                        resultItem.appendChild(videoList);

                        // Insert new result at the top of the container
                        if (resultsContainer.firstChild) """),format.raw/*313.58*/("""{"""),format.raw/*313.59*/("""
                            """),format.raw/*314.29*/("""resultsContainer.insertBefore(resultItem, resultsContainer.firstChild);
                        """),format.raw/*315.25*/("""}"""),format.raw/*315.26*/(""" """),format.raw/*315.27*/("""else """),format.raw/*315.32*/("""{"""),format.raw/*315.33*/("""
                            """),format.raw/*316.29*/("""resultsContainer.appendChild(resultItem);
                        """),format.raw/*317.25*/("""}"""),format.raw/*317.26*/("""

                        """),format.raw/*319.25*/("""// Increment search results count
                        searchResultsCount++;

                        // If we exceed max results, remove from the bottom
                        if (searchResultsCount > MAX_SEARCH_RESULTS) """),format.raw/*323.70*/("""{"""),format.raw/*323.71*/("""
                            """),format.raw/*324.29*/("""resultsContainer.removeChild(resultsContainer.lastElementChild);
                            searchResultsCount = MAX_SEARCH_RESULTS;
                        """),format.raw/*326.25*/("""}"""),format.raw/*326.26*/("""
                    """),format.raw/*327.21*/("""}"""),format.raw/*327.22*/("""
                    """),format.raw/*328.21*/("""// Initialize WebSocket on page load
                    initWebSocket();

                    // Search form submission
                    searchForm.addEventListener('submit', (e) => """),format.raw/*332.66*/("""{"""),format.raw/*332.67*/("""
                        """),format.raw/*333.25*/("""e.preventDefault();
                        const query = searchQuery.value.trim();

                        if (!query) """),format.raw/*336.37*/("""{"""),format.raw/*336.38*/("""
                            """),format.raw/*337.29*/("""errorContainer.textContent = 'Please enter a search term';
                            return;
                        """),format.raw/*339.25*/("""}"""),format.raw/*339.26*/("""

                        """),format.raw/*341.25*/("""if (!socket || socket.readyState !== WebSocket.OPEN) """),format.raw/*341.78*/("""{"""),format.raw/*341.79*/("""
                            """),format.raw/*342.29*/("""errorContainer.textContent = 'WebSocket is not connected. Please try again.';
                            return;
                        """),format.raw/*344.25*/("""}"""),format.raw/*344.26*/("""

                        """),format.raw/*346.25*/("""// Show loading indicator
                        loadingIndicator.style.display = 'block';
                        errorContainer.textContent = '';

                        // Send search query via WebSocket
                        socket.send(query);
                        // Clear input after sending
                        searchQuery.value = '';
                    """),format.raw/*354.21*/("""}"""),format.raw/*354.22*/(""");
                """),format.raw/*355.17*/("""}"""),format.raw/*355.18*/(""");
        </script>
    </body>
</html>"""))
      }
    }
  }

  def render(searchHistory:List[VideoSearchData],request:RequestHeader): play.twirl.api.HtmlFormat.Appendable = apply(searchHistory)(request)

  def f:((List[VideoSearchData]) => (RequestHeader) => play.twirl.api.HtmlFormat.Appendable) = (searchHistory) => (request) => apply(searchHistory)(request)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  SOURCE: app/views/index.scala.html
                  HASH: 6050b25c0d2478b7ed155eef8da35ece297f3f2b
                  MATRIX: 610->1|652->38|1020->75|1186->146|1216->150|1487->394|1502->400|1564->441|1630->479|1659->480|1705->498|1943->708|1972->709|2014->723|2051->732|2080->733|2126->751|2392->989|2421->990|2463->1004|2503->1016|2532->1017|2578->1035|2690->1119|2719->1120|2761->1134|2807->1152|2836->1153|2882->1171|3050->1311|3079->1312|3121->1326|3172->1349|3201->1350|3247->1368|3447->1540|3476->1541|3518->1555|3570->1579|3599->1580|3645->1598|3947->1872|3976->1873|4018->1887|4076->1917|4105->1918|4151->1936|4273->2030|4302->2031|4344->2045|4397->2070|4426->2071|4472->2089|4778->2367|4807->2368|4849->2382|4908->2413|4937->2414|4983->2432|5077->2498|5106->2499|5148->2513|5195->2532|5224->2533|5270->2551|5394->2647|5423->2648|5465->2662|5514->2683|5543->2684|5589->2702|5682->2767|5711->2768|5753->2782|5807->2808|5836->2809|5882->2827|6220->3137|6249->3138|6291->3152|6331->3164|6360->3165|6406->3183|6499->3248|6528->3249|6570->3263|6610->3275|6639->3276|6685->3294|6992->3573|7021->3574|7063->3588|7107->3604|7136->3605|7182->3623|7306->3718|7336->3719|7379->3733|7420->3745|7450->3746|7497->3764|7585->3823|7615->3824|7658->3838|7701->3852|7731->3853|7778->3871|7896->3960|7926->3961|7969->3975|8018->3995|8048->3996|8095->4014|8165->4055|8195->4056|8238->4070|8286->4089|8316->4090|8363->4108|8490->4206|8520->4207|8563->4221|8609->4238|8639->4239|8686->4257|8772->4314|8802->4315|8840->4325|9661->5155|9704->5170|9750->5199|9791->5201|9923->5304|9965->5306|10016->5328|10107->5391|10124->5398|10157->5409|10231->5455|10248->5462|10285->5477|10368->5532|10385->5539|10425->5557|10510->5614|10527->5621|10568->5640|10627->5671|10677->5704|10718->5706|10777->5736|10868->5799|10883->5804|10916->5815|10996->5867|11011->5872|11051->5890|11150->5957|11201->5979|11271->6004|11318->6019|11356->6029|11481->6125|11511->6126|11562->6148|12130->6688|12146->6694|12216->6742|12296->6793|12326->6794|12381->6820|12546->6956|12576->6957|12635->6987|12829->7152|12859->7153|12947->7212|12977->7213|13036->7243|13069->7247|13099->7248|13162->7282|13318->7409|13348->7410|13378->7411|13421->7425|13451->7426|13514->7460|13717->7634|13747->7635|13777->7636|13814->7644|13844->7645|13907->7679|14006->7749|14036->7750|14091->7776|14121->7777|14207->7834|14237->7835|14296->7865|14610->8150|14640->8151|14726->8208|14756->8209|14815->8239|15069->8464|15099->8465|15151->8488|15181->8489|15234->8513|15408->8658|15438->8659|15493->8685|15653->8816|15683->8817|15742->8847|15890->8966|15920->8967|15977->8995|16220->9209|16250->9210|16309->9240|16469->9371|16499->9372|16545->9388|16576->9389|16609->9392|16640->9393|16703->9427|16897->9592|16927->9593|16982->9619|17012->9620|17423->10002|17453->10003|17498->10019|17528->10020|17766->10229|17796->10230|17854->10259|17884->10260|18134->10481|18164->10482|18223->10512|18388->10648|18418->10649|18488->10689|18519->10690|18646->10788|18676->10789|18733->10817|18810->10865|18840->10866|18899->10896|19061->11029|19091->11030|19160->11069|19191->11070|19317->11167|19347->11168|19406->11198|19648->11411|19678->11412|19737->11442|21036->12712|21066->12713|21110->12728|21140->12729|21298->12858|21328->12859|21375->12877|21405->12878|21662->13106|21692->13107|21759->13144|21790->13145|22030->13356|22060->13357|22102->13370|22132->13371|22770->13980|22800->13981|23028->14180|23058->14181|23117->14211|23243->14308|23273->14309|23303->14310|23337->14315|23367->14316|23426->14346|23522->14413|23552->14414|23609->14442|23868->14672|23898->14673|23957->14703|24146->14863|24176->14864|24227->14886|24257->14887|24308->14909|24527->15099|24557->15100|24612->15126|24765->15250|24795->15251|24854->15281|25004->15402|25034->15403|25091->15431|25173->15484|25203->15485|25262->15515|25431->15655|25461->15656|25518->15684|25929->16066|25959->16067|26008->16087|26038->16088
                  LINES: 23->1|24->2|29->3|34->3|36->5|42->11|42->11|42->11|44->13|44->13|45->14|50->19|50->19|51->20|51->20|51->20|52->21|57->26|57->26|58->27|58->27|58->27|59->28|61->30|61->30|62->31|62->31|62->31|63->32|67->36|67->36|68->37|68->37|68->37|69->38|74->43|74->43|75->44|75->44|75->44|76->45|83->52|83->52|84->53|84->53|84->53|85->54|87->56|87->56|88->57|88->57|88->57|89->58|96->65|96->65|97->66|97->66|97->66|98->67|99->68|99->68|100->69|100->69|100->69|101->70|104->73|104->73|105->74|105->74|105->74|106->75|108->77|108->77|109->78|109->78|109->78|110->79|116->85|116->85|117->86|117->86|117->86|118->87|120->89|120->89|121->90|121->90|121->90|122->91|128->97|128->97|129->98|129->98|129->98|130->99|133->102|133->102|134->103|134->103|134->103|135->104|137->106|137->106|138->107|138->107|138->107|139->108|141->110|141->110|142->111|142->111|142->111|143->112|144->113|144->113|145->114|145->114|145->114|146->115|148->117|148->117|149->118|149->118|149->118|150->119|151->120|151->120|152->121|177->146|178->147|178->147|178->147|179->148|179->148|180->149|181->150|181->150|181->150|182->151|182->151|182->151|183->152|183->152|183->152|184->153|184->153|184->153|185->154|185->154|185->154|186->155|187->156|187->156|187->156|188->157|188->157|188->157|190->159|191->160|192->161|193->162|194->163|197->166|197->166|198->167|206->175|206->175|206->175|208->177|208->177|209->178|212->181|212->181|213->182|215->184|215->184|217->186|217->186|218->187|218->187|218->187|219->188|221->190|221->190|221->190|221->190|221->190|222->191|224->193|224->193|224->193|224->193|224->193|225->194|226->195|226->195|227->196|227->196|229->198|229->198|230->199|234->203|234->203|236->205|236->205|237->206|240->209|240->209|241->210|241->210|243->212|246->215|246->215|247->216|248->217|248->217|249->218|251->220|251->220|253->222|255->224|255->224|256->225|257->226|257->226|257->226|257->226|257->226|257->226|258->227|260->229|260->229|261->230|261->230|269->238|269->238|269->238|269->238|273->242|273->242|273->242|273->242|277->246|277->246|278->247|279->248|279->248|279->248|279->248|281->250|281->250|283->252|283->252|283->252|284->253|285->254|285->254|285->254|285->254|287->256|287->256|290->259|294->263|294->263|295->264|316->285|316->285|316->285|316->285|318->287|318->287|318->287|318->287|322->291|322->291|322->291|322->291|326->295|326->295|326->295|326->295|339->308|339->308|344->313|344->313|345->314|346->315|346->315|346->315|346->315|346->315|347->316|348->317|348->317|350->319|354->323|354->323|355->324|357->326|357->326|358->327|358->327|359->328|363->332|363->332|364->333|367->336|367->336|368->337|370->339|370->339|372->341|372->341|372->341|373->342|375->344|375->344|377->346|385->354|385->354|386->355|386->355
                  -- GENERATED --
              */
          