
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
/*1.2*/import models.data.VideoData

object videoList extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template2[String,List[VideoData],play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*3.2*/(tag: String, videos: List[VideoData]):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*4.1*/("""
"""),_display_(/*5.2*/main("Videos for @tag")/*5.25*/ {_display_(Seq[Any](format.raw/*5.27*/("""
    """),format.raw/*6.5*/("""<link rel="icon" href=""""),_display_(/*6.29*/routes/*6.35*/.Assets.versioned("images/img.png")),format.raw/*6.70*/("""" type="image/png">
    <link rel="script" href=""""),_display_(/*7.31*/routes/*7.37*/.Assets.versioned("javascipts/main.js")),format.raw/*7.76*/("""">
    <link rel="stylesheet" href=""""),_display_(/*8.35*/routes/*8.41*/.Assets.versioned("stylesheets/main.css")),format.raw/*8.82*/("""">

    <h2>Videos for Tag: """),_display_(/*10.26*/tag),format.raw/*10.29*/("""</h2>
    <ul>
    """),_display_(/*12.6*/for(video <- videos) yield /*12.26*/ {_display_(Seq[Any](format.raw/*12.28*/("""
        """),format.raw/*13.9*/("""<li>
            <a href=""""),_display_(/*14.23*/video/*14.28*/.getVideoUrl()),format.raw/*14.42*/("""">"""),_display_(/*14.45*/video/*14.50*/.getTitle()),format.raw/*14.61*/("""</a><br>
            <img src=""""),_display_(/*15.24*/video/*15.29*/.getThumbnailUrl()),format.raw/*15.47*/("""" alt="Thumbnail"><br>
            <p>"""),_display_(if(video.getDescription().nonEmpty)/*16.52*/ {_display_(Seq[Any](format.raw/*16.54*/(""" """),_display_(/*16.56*/video/*16.61*/.getDescription()),format.raw/*16.78*/(""" """)))}else/*16.86*/{_display_(Seq[Any](format.raw/*16.87*/(""" """),format.raw/*16.88*/("""<em>No description available</em> """)))}),format.raw/*16.123*/("""</p>
        </li>
    """)))}),format.raw/*18.6*/("""
    """),format.raw/*19.5*/("""</ul>
""")))}))
      }
    }
  }

  def render(tag:String,videos:List[VideoData]): play.twirl.api.HtmlFormat.Appendable = apply(tag,videos)

  def f:((String,List[VideoData]) => play.twirl.api.HtmlFormat.Appendable) = (tag,videos) => apply(tag,videos)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  SOURCE: app/views/videoList.scala.html
                  HASH: 109c0862b43f24b9e9856ef67d374057228b806a
                  MATRIX: 610->1|963->32|1095->71|1122->73|1153->96|1192->98|1223->103|1273->127|1287->133|1342->168|1418->218|1432->224|1491->263|1554->300|1568->306|1629->347|1685->376|1709->379|1755->399|1791->419|1831->421|1867->430|1921->457|1935->462|1970->476|2000->479|2014->484|2046->495|2105->527|2119->532|2158->550|2259->624|2299->626|2328->628|2342->633|2380->650|2405->658|2444->659|2473->660|2540->695|2594->719|2626->724
                  LINES: 23->1|28->3|33->4|34->5|34->5|34->5|35->6|35->6|35->6|35->6|36->7|36->7|36->7|37->8|37->8|37->8|39->10|39->10|41->12|41->12|41->12|42->13|43->14|43->14|43->14|43->14|43->14|43->14|44->15|44->15|44->15|45->16|45->16|45->16|45->16|45->16|45->16|45->16|45->16|45->16|47->18|48->19
                  -- GENERATED --
              */
          