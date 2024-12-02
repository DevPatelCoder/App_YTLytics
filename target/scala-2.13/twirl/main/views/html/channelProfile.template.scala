
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
/*1.2*/import models.data.{ChannelMetaData, VideoData}

object channelProfile extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template2[ChannelMetaData,List[VideoData],play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*2.2*/(channelInfo: ChannelMetaData, recentVideos: List[VideoData]):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*2.63*/("""

"""),_display_(/*4.2*/main("Channel Profile")/*4.25*/ {_display_(Seq[Any](format.raw/*4.27*/("""
"""),format.raw/*5.1*/("""<link rel="icon" href=""""),_display_(/*5.25*/routes/*5.31*/.Assets.versioned("images/img.png")),format.raw/*5.66*/("""" type="image/png">
<link rel="script" href=""""),_display_(/*6.27*/routes/*6.33*/.Assets.versioned("javascipts/main.js")),format.raw/*6.72*/("""">

<style>
    /* General Page Styles */
    body """),format.raw/*10.10*/("""{"""),format.raw/*10.11*/("""
        """),format.raw/*11.9*/("""font-family: Arial, sans-serif;
        background-color: #fff; /* White background for theme */
        color: #333;
        margin: 0;
        padding: 0;
    """),format.raw/*16.5*/("""}"""),format.raw/*16.6*/("""

    """),format.raw/*18.5*/("""h1, h2 """),format.raw/*18.12*/("""{"""),format.raw/*18.13*/("""
        """),format.raw/*19.9*/("""color: #d32f2f; /* Red for headings */
    """),format.raw/*20.5*/("""}"""),format.raw/*20.6*/("""

    """),format.raw/*22.5*/("""a """),format.raw/*22.7*/("""{"""),format.raw/*22.8*/("""
        """),format.raw/*23.9*/("""text-decoration: none;
        color: #d32f2f; /* Red for links */
    """),format.raw/*25.5*/("""}"""),format.raw/*25.6*/("""

    """),format.raw/*27.5*/("""a:hover """),format.raw/*27.13*/("""{"""),format.raw/*27.14*/("""
        """),format.raw/*28.9*/("""text-decoration: underline;
    """),format.raw/*29.5*/("""}"""),format.raw/*29.6*/("""

    """),format.raw/*31.5*/(""".container """),format.raw/*31.16*/("""{"""),format.raw/*31.17*/("""
        """),format.raw/*32.9*/("""width: 80%;
        margin: 0 auto;
    """),format.raw/*34.5*/("""}"""),format.raw/*34.6*/("""

    """),format.raw/*36.5*/(""".channel-header """),format.raw/*36.21*/("""{"""),format.raw/*36.22*/("""
        """),format.raw/*37.9*/("""background-color: #fff; /* White background */
        padding: 20px;
        border-radius: 8px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        margin-top: 20px;
    """),format.raw/*42.5*/("""}"""),format.raw/*42.6*/("""

    """),format.raw/*44.5*/(""".channel-title """),format.raw/*44.20*/("""{"""),format.raw/*44.21*/("""
        """),format.raw/*45.9*/("""font-size: 2.5rem;
        margin-bottom: 10px;
    """),format.raw/*47.5*/("""}"""),format.raw/*47.6*/("""

    """),format.raw/*49.5*/(""".channel-description """),format.raw/*49.26*/("""{"""),format.raw/*49.27*/("""
        """),format.raw/*50.9*/("""font-size: 1.2rem;
        margin-bottom: 20px;
        color: #555;
    """),format.raw/*53.5*/("""}"""),format.raw/*53.6*/("""

    """),format.raw/*55.5*/(""".channel-stats p """),format.raw/*55.22*/("""{"""),format.raw/*55.23*/("""
        """),format.raw/*56.9*/("""font-size: 1rem;
        color: #777;
        margin: 5px 0;
    """),format.raw/*59.5*/("""}"""),format.raw/*59.6*/("""

    """),format.raw/*61.5*/(""".recent-videos-title """),format.raw/*61.26*/("""{"""),format.raw/*61.27*/("""
        """),format.raw/*62.9*/("""font-size: 2rem;
        margin: 40px 0 20px 0;
    """),format.raw/*64.5*/("""}"""),format.raw/*64.6*/("""

    """),format.raw/*66.5*/(""".video-list """),format.raw/*66.17*/("""{"""),format.raw/*66.18*/("""
        """),format.raw/*67.9*/("""list-style-type: none;
        padding: 0;
        margin: 0;
    """),format.raw/*70.5*/("""}"""),format.raw/*70.6*/("""

    """),format.raw/*72.5*/(""".video-item """),format.raw/*72.17*/("""{"""),format.raw/*72.18*/("""
        """),format.raw/*73.9*/("""display: flex;
        align-items: center;
        background-color: #fff; /* White background for video items */
        border: 1px solid #d32f2f; /* Red border */
        border-radius: 8px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        overflow: hidden;
        transition: transform 0.3s ease;
        margin-bottom: 20px;
    """),format.raw/*82.5*/("""}"""),format.raw/*82.6*/("""

    """),format.raw/*84.5*/(""".video-item:hover """),format.raw/*84.23*/("""{"""),format.raw/*84.24*/("""
        """),format.raw/*85.9*/("""transform: translateY(-5px);
    """),format.raw/*86.5*/("""}"""),format.raw/*86.6*/("""

    """),format.raw/*88.5*/(""".video-thumbnail """),format.raw/*88.22*/("""{"""),format.raw/*88.23*/("""
        """),format.raw/*89.9*/("""width: 200px;
        height: 150px;
        object-fit: cover;
        border-right: 1px solid #ddd;
    """),format.raw/*93.5*/("""}"""),format.raw/*93.6*/("""

    """),format.raw/*95.5*/(""".video-info """),format.raw/*95.17*/("""{"""),format.raw/*95.18*/("""
        """),format.raw/*96.9*/("""padding: 15px;
        flex-grow: 1;
    """),format.raw/*98.5*/("""}"""),format.raw/*98.6*/("""

    """),format.raw/*100.5*/(""".video-title """),format.raw/*100.18*/("""{"""),format.raw/*100.19*/("""
        """),format.raw/*101.9*/("""font-size: 1.5rem;
        margin-bottom: 10px;
        color: #d32f2f; /* Red for video titles */
    """),format.raw/*104.5*/("""}"""),format.raw/*104.6*/("""

    """),format.raw/*106.5*/(""".video-description """),format.raw/*106.24*/("""{"""),format.raw/*106.25*/("""
        """),format.raw/*107.9*/("""font-size: 1rem;
        color: #666;
        margin-bottom: 10px;
    """),format.raw/*110.5*/("""}"""),format.raw/*110.6*/("""

    """),format.raw/*112.5*/(""".tags """),format.raw/*112.11*/("""{"""),format.raw/*112.12*/("""
        """),format.raw/*113.9*/("""padding: 10px;
        text-align: right;
        border-top: 1px solid #ddd;
    """),format.raw/*116.5*/("""}"""),format.raw/*116.6*/("""

    """),format.raw/*118.5*/(""".tags-link """),format.raw/*118.16*/("""{"""),format.raw/*118.17*/("""
        """),format.raw/*119.9*/("""font-size: 0.9rem;
        color: #d32f2f; /* Red for tags links */
    """),format.raw/*121.5*/("""}"""),format.raw/*121.6*/("""

    """),format.raw/*123.5*/(""".tags-link:hover """),format.raw/*123.22*/("""{"""),format.raw/*123.23*/("""
        """),format.raw/*124.9*/("""text-decoration: underline;
    """),format.raw/*125.5*/("""}"""),format.raw/*125.6*/("""
"""),format.raw/*126.1*/("""</style>

<div class="channel-header">
    <h1 class="channel-title">"""),_display_(/*129.32*/channelInfo/*129.43*/.getTitle()),format.raw/*129.54*/("""</h1>
    <p class="channel-description">"""),_display_(/*130.37*/channelInfo/*130.48*/.getDescription()),format.raw/*130.65*/("""</p>
    <div class="channel-stats">
        <p><strong>Subscribers:</strong> """),_display_(/*132.43*/channelInfo/*132.54*/.getSubscriberCount()),format.raw/*132.75*/("""</p>
        <p><strong>Views:</strong> """),_display_(/*133.37*/channelInfo/*133.48*/.getViewCount()),format.raw/*133.63*/("""</p>
        <p><strong>Videos:</strong> """),_display_(/*134.38*/channelInfo/*134.49*/.getVideoCount()),format.raw/*134.65*/("""</p>
    </div>
</div>

<h2 class="recent-videos-title">Recent Videos</h2>
<ul class="video-list">
    """),_display_(/*140.6*/for(video <- recentVideos) yield /*140.32*/ {_display_(Seq[Any](format.raw/*140.34*/("""
    """),format.raw/*141.5*/("""<li class="video-item">
        <img class="video-thumbnail" src=""""),_display_(/*142.44*/video/*142.49*/.getThumbnailUrl()),format.raw/*142.67*/("""" alt="Thumbnail">
        <div class="video-info">
            <h3 class="video-title">
                <a href=""""),_display_(/*145.27*/video/*145.32*/.getVideoUrl()),format.raw/*145.46*/("""" target="_blank">"""),_display_(/*145.65*/video/*145.70*/.getTitle()),format.raw/*145.81*/("""</a>
            </h3>
            <p class="video-description">
                """),_display_(if(video.getDescription().nonEmpty)/*148.53*/ {_display_(Seq[Any](format.raw/*148.55*/("""
                """),_display_(/*149.18*/video/*149.23*/.getDescription()),format.raw/*149.40*/("""
                """)))}else/*150.24*/{_display_(Seq[Any](format.raw/*150.25*/("""
                """),format.raw/*151.17*/("""<em>No description available</em>
                """)))}),format.raw/*152.18*/("""
            """),format.raw/*153.13*/("""</p>
            <div class="tags">
                <a href=""""),_display_(/*155.27*/routes/*155.33*/.HomeController.showTags(video.getVideoId())),format.raw/*155.77*/("""" class="tags-link">View Tags</a>
            </div>
        </div>
    </li>
    """)))}),format.raw/*159.6*/("""
"""),format.raw/*160.1*/("""</ul>
""")))}),format.raw/*161.2*/("""
"""))
      }
    }
  }

  def render(channelInfo:ChannelMetaData,recentVideos:List[VideoData]): play.twirl.api.HtmlFormat.Appendable = apply(channelInfo,recentVideos)

  def f:((ChannelMetaData,List[VideoData]) => play.twirl.api.HtmlFormat.Appendable) = (channelInfo,recentVideos) => apply(channelInfo,recentVideos)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  SOURCE: app/views/channelProfile.scala.html
                  HASH: 3cea4eeb392cf02aca814c880b9ad0c9a5e01017
                  MATRIX: 610->1|996->51|1152->112|1182->117|1213->140|1252->142|1280->144|1330->168|1344->174|1399->209|1472->256|1486->262|1545->301|1628->356|1657->357|1694->367|1887->533|1915->534|1950->542|1985->549|2014->550|2051->560|2122->604|2150->605|2185->613|2214->615|2242->616|2279->626|2379->699|2407->700|2442->708|2478->716|2507->717|2544->727|2604->760|2632->761|2667->769|2706->780|2735->781|2772->791|2841->833|2869->834|2904->842|2948->858|2977->859|3014->869|3224->1052|3252->1053|3287->1061|3330->1076|3359->1077|3396->1087|3477->1141|3505->1142|3540->1150|3589->1171|3618->1172|3655->1182|3758->1258|3786->1259|3821->1267|3866->1284|3895->1285|3932->1295|4027->1363|4055->1364|4090->1372|4139->1393|4168->1394|4205->1404|4286->1458|4314->1459|4349->1467|4389->1479|4418->1480|4455->1490|4551->1559|4579->1560|4614->1568|4654->1580|4683->1581|4720->1591|5101->1945|5129->1946|5164->1954|5210->1972|5239->1973|5276->1983|5337->2017|5365->2018|5400->2026|5445->2043|5474->2044|5511->2054|5648->2164|5676->2165|5711->2173|5751->2185|5780->2186|5817->2196|5887->2239|5915->2240|5951->2248|5993->2261|6023->2262|6061->2272|6195->2378|6224->2379|6260->2387|6308->2406|6338->2407|6376->2417|6478->2491|6507->2492|6543->2500|6578->2506|6608->2507|6646->2517|6759->2602|6788->2603|6824->2611|6864->2622|6894->2623|6932->2633|7034->2707|7063->2708|7099->2716|7145->2733|7175->2734|7213->2744|7274->2777|7303->2778|7333->2780|7434->2853|7455->2864|7488->2875|7559->2918|7580->2929|7619->2946|7728->3027|7749->3038|7792->3059|7862->3101|7883->3112|7920->3127|7991->3170|8012->3181|8050->3197|8187->3307|8230->3333|8271->3335|8305->3341|8401->3409|8416->3414|8456->3432|8602->3550|8617->3555|8653->3569|8700->3588|8715->3593|8748->3604|8896->3724|8937->3726|8984->3745|8999->3750|9038->3767|9081->3792|9121->3793|9168->3811|9252->3863|9295->3877|9387->3941|9403->3947|9469->3991|9587->4078|9617->4080|9656->4088
                  LINES: 23->1|28->2|33->2|35->4|35->4|35->4|36->5|36->5|36->5|36->5|37->6|37->6|37->6|41->10|41->10|42->11|47->16|47->16|49->18|49->18|49->18|50->19|51->20|51->20|53->22|53->22|53->22|54->23|56->25|56->25|58->27|58->27|58->27|59->28|60->29|60->29|62->31|62->31|62->31|63->32|65->34|65->34|67->36|67->36|67->36|68->37|73->42|73->42|75->44|75->44|75->44|76->45|78->47|78->47|80->49|80->49|80->49|81->50|84->53|84->53|86->55|86->55|86->55|87->56|90->59|90->59|92->61|92->61|92->61|93->62|95->64|95->64|97->66|97->66|97->66|98->67|101->70|101->70|103->72|103->72|103->72|104->73|113->82|113->82|115->84|115->84|115->84|116->85|117->86|117->86|119->88|119->88|119->88|120->89|124->93|124->93|126->95|126->95|126->95|127->96|129->98|129->98|131->100|131->100|131->100|132->101|135->104|135->104|137->106|137->106|137->106|138->107|141->110|141->110|143->112|143->112|143->112|144->113|147->116|147->116|149->118|149->118|149->118|150->119|152->121|152->121|154->123|154->123|154->123|155->124|156->125|156->125|157->126|160->129|160->129|160->129|161->130|161->130|161->130|163->132|163->132|163->132|164->133|164->133|164->133|165->134|165->134|165->134|171->140|171->140|171->140|172->141|173->142|173->142|173->142|176->145|176->145|176->145|176->145|176->145|176->145|179->148|179->148|180->149|180->149|180->149|181->150|181->150|182->151|183->152|184->153|186->155|186->155|186->155|190->159|191->160|192->161
                  -- GENERATED --
              */
          