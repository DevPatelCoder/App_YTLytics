
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

object tags extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template2[String,List[String],play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(videoId: String, tags: List[String]):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.39*/("""

"""),_display_(/*3.2*/main("Video Tags")/*3.20*/ {_display_(Seq[Any](format.raw/*3.22*/("""
    """),format.raw/*4.5*/("""<link rel="icon" href=""""),_display_(/*4.29*/routes/*4.35*/.Assets.versioned("images/img.png")),format.raw/*4.70*/("""" type="image/png">
    <link rel="script" href=""""),_display_(/*5.31*/routes/*5.37*/.Assets.versioned("javascipts/main.js")),format.raw/*5.76*/("""">
    <link rel="stylesheet" href=""""),_display_(/*6.35*/routes/*6.41*/.Assets.versioned("stylesheets/main.css")),format.raw/*6.82*/("""">

    <h1>Tags for Video: """),_display_(/*8.26*/videoId),format.raw/*8.33*/("""</h1>
    <ul>
    """),_display_(/*10.6*/for(tag <- tags) yield /*10.22*/ {_display_(Seq[Any](format.raw/*10.24*/("""
        """),format.raw/*11.9*/("""<li>
            <a href=""""),_display_(/*12.23*/routes/*12.29*/.HomeController.showVideosByTag(tag)),format.raw/*12.65*/("""">
            """),_display_(/*13.14*/tag),format.raw/*13.17*/("""
            """),format.raw/*14.13*/("""</a>
        </li>
    """)))}),format.raw/*16.6*/("""
    """),format.raw/*17.5*/("""</ul>
""")))}))
      }
    }
  }

  def render(videoId:String,tags:List[String]): play.twirl.api.HtmlFormat.Appendable = apply(videoId,tags)

  def f:((String,List[String]) => play.twirl.api.HtmlFormat.Appendable) = (videoId,tags) => apply(videoId,tags)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  SOURCE: app/views/tags.scala.html
                  HASH: aa754db103ef808fec62516bb707a3c3ec82c825
                  MATRIX: 919->1|1051->38|1081->43|1107->61|1146->63|1178->69|1228->93|1242->99|1297->134|1374->185|1388->191|1447->230|1511->268|1525->274|1586->315|1643->346|1670->353|1718->375|1750->391|1790->393|1827->403|1882->431|1897->437|1954->473|1998->490|2022->493|2064->507|2120->533|2153->539
                  LINES: 27->1|32->1|34->3|34->3|34->3|35->4|35->4|35->4|35->4|36->5|36->5|36->5|37->6|37->6|37->6|39->8|39->8|41->10|41->10|41->10|42->11|43->12|43->12|43->12|44->13|44->13|45->14|47->16|48->17
                  -- GENERATED --
              */
          