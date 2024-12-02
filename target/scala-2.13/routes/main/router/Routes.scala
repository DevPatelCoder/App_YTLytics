// @GENERATOR:play-routes-compiler
// @SOURCE:conf/routes

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._

import play.api.mvc._

import _root_.controllers.Assets.Asset
import _root_.play.libs.F

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:7
  HomeController_0: controllers.HomeController,
  // @LINE:10
  Assets_1: controllers.Assets,
  val prefix: String
) extends GeneratedRouter {

  @javax.inject.Inject()
  def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:7
    HomeController_0: controllers.HomeController,
    // @LINE:10
    Assets_1: controllers.Assets
  ) = this(errorHandler, HomeController_0, Assets_1, "/")

  def withPrefix(addPrefix: String): Routes = {
    val prefix = play.api.routing.Router.concatPrefix(addPrefix, this.prefix)
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, HomeController_0, Assets_1, prefix)
  }

  private[this] val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""GET""", this.prefix, """controllers.HomeController.index(request:Request)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """assets/""" + "$" + """file<.+>""", """controllers.Assets.versioned(path:String = "/public", file:Asset)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """channel/""" + "$" + """channelId<[^/]+>""", """controllers.HomeController.channelProfile(channelId:String, request:Request)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """tags/""" + "$" + """videoId<[^/]+>""", """controllers.HomeController.showTags(videoId:String)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """word-stat/""" + "$" + """query<[^/]+>""", """controllers.HomeController.getWordStats(query:String)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """search/clear""", """controllers.HomeController.clearHistory(request:Request)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """videosByTag/""" + "$" + """tag<[^/]+>""", """controllers.HomeController.showVideosByTag(tag:String)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """assets/""" + "$" + """file<.+>""", """controllers.Assets.versioned(path:String = "/public", file:Asset)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """ws/search""", """controllers.HomeController.searchWebSocket()"""),
    Nil
  ).foldLeft(Seq.empty[(String, String, String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String, String, String)]
    case l => s ++ l.asInstanceOf[List[(String, String, String)]]
  }}


  // @LINE:7
  private[this] lazy val controllers_HomeController_index0_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix)))
  )
  private[this] lazy val controllers_HomeController_index0_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      HomeController_0.index(fakeValue[play.mvc.Http.Request]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "index",
      Seq(classOf[play.mvc.Http.Request]),
      "GET",
      this.prefix + """""",
      """""",
      Seq()
    )
  )

  // @LINE:10
  private[this] lazy val controllers_Assets_versioned1_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("assets/"), DynamicPart("file", """.+""",false)))
  )
  private[this] lazy val controllers_Assets_versioned1_invoker = createInvoker(
    Assets_1.versioned(fakeValue[String], fakeValue[Asset]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Assets",
      "versioned",
      Seq(classOf[String], classOf[Asset]),
      "GET",
      this.prefix + """assets/""" + "$" + """file<.+>""",
      """""",
      Seq()
    )
  )

  // @LINE:11
  private[this] lazy val controllers_HomeController_channelProfile2_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("channel/"), DynamicPart("channelId", """[^/]+""",true)))
  )
  private[this] lazy val controllers_HomeController_channelProfile2_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      HomeController_0.channelProfile(fakeValue[String], fakeValue[play.mvc.Http.Request]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "channelProfile",
      Seq(classOf[String], classOf[play.mvc.Http.Request]),
      "GET",
      this.prefix + """channel/""" + "$" + """channelId<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:12
  private[this] lazy val controllers_HomeController_showTags3_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("tags/"), DynamicPart("videoId", """[^/]+""",true)))
  )
  private[this] lazy val controllers_HomeController_showTags3_invoker = createInvoker(
    HomeController_0.showTags(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "showTags",
      Seq(classOf[String]),
      "GET",
      this.prefix + """tags/""" + "$" + """videoId<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:13
  private[this] lazy val controllers_HomeController_getWordStats4_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("word-stat/"), DynamicPart("query", """[^/]+""",true)))
  )
  private[this] lazy val controllers_HomeController_getWordStats4_invoker = createInvoker(
    HomeController_0.getWordStats(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "getWordStats",
      Seq(classOf[String]),
      "GET",
      this.prefix + """word-stat/""" + "$" + """query<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:14
  private[this] lazy val controllers_HomeController_clearHistory5_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("search/clear")))
  )
  private[this] lazy val controllers_HomeController_clearHistory5_invoker = createInvoker(
    
    (req:play.mvc.Http.Request) =>
      HomeController_0.clearHistory(fakeValue[play.mvc.Http.Request]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "clearHistory",
      Seq(classOf[play.mvc.Http.Request]),
      "GET",
      this.prefix + """search/clear""",
      """""",
      Seq()
    )
  )

  // @LINE:15
  private[this] lazy val controllers_HomeController_showVideosByTag6_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("videosByTag/"), DynamicPart("tag", """[^/]+""",true)))
  )
  private[this] lazy val controllers_HomeController_showVideosByTag6_invoker = createInvoker(
    HomeController_0.showVideosByTag(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "showVideosByTag",
      Seq(classOf[String]),
      "GET",
      this.prefix + """videosByTag/""" + "$" + """tag<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:17
  private[this] lazy val controllers_Assets_versioned7_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("assets/"), DynamicPart("file", """.+""",false)))
  )
  private[this] lazy val controllers_Assets_versioned7_invoker = createInvoker(
    Assets_1.versioned(fakeValue[String], fakeValue[Asset]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Assets",
      "versioned",
      Seq(classOf[String], classOf[Asset]),
      "GET",
      this.prefix + """assets/""" + "$" + """file<.+>""",
      """""",
      Seq()
    )
  )

  // @LINE:19
  private[this] lazy val controllers_HomeController_searchWebSocket8_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("ws/search")))
  )
  private[this] lazy val controllers_HomeController_searchWebSocket8_invoker = createInvoker(
    HomeController_0.searchWebSocket(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "searchWebSocket",
      Nil,
      "GET",
      this.prefix + """ws/search""",
      """""",
      Seq()
    )
  )


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:7
    case controllers_HomeController_index0_route(params@_) =>
      call { 
        controllers_HomeController_index0_invoker.call(
          req => HomeController_0.index(req))
      }
  
    // @LINE:10
    case controllers_Assets_versioned1_route(params@_) =>
      call(Param[String]("path", Right("/public")), params.fromPath[Asset]("file", None)) { (path, file) =>
        controllers_Assets_versioned1_invoker.call(Assets_1.versioned(path, file))
      }
  
    // @LINE:11
    case controllers_HomeController_channelProfile2_route(params@_) =>
      call(params.fromPath[String]("channelId", None)) { (channelId) =>
        controllers_HomeController_channelProfile2_invoker.call(
          req => HomeController_0.channelProfile(channelId, req))
      }
  
    // @LINE:12
    case controllers_HomeController_showTags3_route(params@_) =>
      call(params.fromPath[String]("videoId", None)) { (videoId) =>
        controllers_HomeController_showTags3_invoker.call(HomeController_0.showTags(videoId))
      }
  
    // @LINE:13
    case controllers_HomeController_getWordStats4_route(params@_) =>
      call(params.fromPath[String]("query", None)) { (query) =>
        controllers_HomeController_getWordStats4_invoker.call(HomeController_0.getWordStats(query))
      }
  
    // @LINE:14
    case controllers_HomeController_clearHistory5_route(params@_) =>
      call { 
        controllers_HomeController_clearHistory5_invoker.call(
          req => HomeController_0.clearHistory(req))
      }
  
    // @LINE:15
    case controllers_HomeController_showVideosByTag6_route(params@_) =>
      call(params.fromPath[String]("tag", None)) { (tag) =>
        controllers_HomeController_showVideosByTag6_invoker.call(HomeController_0.showVideosByTag(tag))
      }
  
    // @LINE:17
    case controllers_Assets_versioned7_route(params@_) =>
      call(Param[String]("path", Right("/public")), params.fromPath[Asset]("file", None)) { (path, file) =>
        controllers_Assets_versioned7_invoker.call(Assets_1.versioned(path, file))
      }
  
    // @LINE:19
    case controllers_HomeController_searchWebSocket8_route(params@_) =>
      call { 
        controllers_HomeController_searchWebSocket8_invoker.call(HomeController_0.searchWebSocket())
      }
  }
}
