// @GENERATOR:play-routes-compiler
// @SOURCE:conf/routes

import play.api.mvc.Call


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:7
package controllers {

  // @LINE:7
  class ReverseHomeController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:11
    def channelProfile(channelId:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "channel/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("channelId", channelId)))
    }
  
    // @LINE:14
    def clearHistory(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "search/clear")
    }
  
    // @LINE:13
    def getWordStats(query:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "word-stat/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("query", query)))
    }
  
    // @LINE:19
    def searchWebSocket(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "ws/search")
    }
  
    // @LINE:15
    def showVideosByTag(tag:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "videosByTag/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("tag", tag)))
    }
  
    // @LINE:12
    def showTags(videoId:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "tags/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("videoId", videoId)))
    }
  
    // @LINE:7
    def index(): Call = {
      
      Call("GET", _prefix)
    }
  
  }

  // @LINE:10
  class ReverseAssets(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:10
    def versioned(file:Asset): Call = {
    
      (file: @unchecked) match {
      
        // @LINE:10
        case (file)  =>
          implicit lazy val _rrc = new play.core.routing.ReverseRouteContext(Map(("path", "/public"))); _rrc
          Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[play.api.mvc.PathBindable[Asset]].unbind("file", file))
      
      }
    
    }
  
  }


}
