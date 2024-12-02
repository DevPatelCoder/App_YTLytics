// @GENERATOR:play-routes-compiler
// @SOURCE:conf/routes

import play.api.routing.JavaScriptReverseRoute


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:7
package controllers.javascript {

  // @LINE:7
  class ReverseHomeController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:11
    def channelProfile: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.channelProfile",
      """
        function(channelId0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "channel/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("channelId", channelId0))})
        }
      """
    )
  
    // @LINE:14
    def clearHistory: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.clearHistory",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "search/clear"})
        }
      """
    )
  
    // @LINE:13
    def getWordStats: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.getWordStats",
      """
        function(query0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "word-stat/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("query", query0))})
        }
      """
    )
  
    // @LINE:19
    def searchWebSocket: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.searchWebSocket",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "ws/search"})
        }
      """
    )
  
    // @LINE:15
    def showVideosByTag: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.showVideosByTag",
      """
        function(tag0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "videosByTag/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("tag", tag0))})
        }
      """
    )
  
    // @LINE:12
    def showTags: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.showTags",
      """
        function(videoId0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "tags/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("videoId", videoId0))})
        }
      """
    )
  
    // @LINE:7
    def index: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.index",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + """"})
        }
      """
    )
  
  }

  // @LINE:10
  class ReverseAssets(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:10
    def versioned: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.Assets.versioned",
      """
        function(file1) {
        
          if (true) {
            return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "assets/" + (""" + implicitly[play.api.mvc.PathBindable[Asset]].javascriptUnbind + """)("file", file1)})
          }
        
        }
      """
    )
  
  }


}
