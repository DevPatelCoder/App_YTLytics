
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
/*1.2*/import models.data.WordStatData

object wordStats extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template1[WordStatData,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*2.2*/(wordStats: WordStatData):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*2.27*/("""

"""),_display_(/*4.2*/main("Word Stats")/*4.20*/ {_display_(Seq[Any](format.raw/*4.22*/("""
"""),format.raw/*5.1*/("""<style>
        body """),format.raw/*6.14*/("""{"""),format.raw/*6.15*/("""
            """),format.raw/*7.13*/("""font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f4f7fa;
            margin: 0;
            padding: 0;
            color: #333;
        """),format.raw/*12.9*/("""}"""),format.raw/*12.10*/("""

        """),format.raw/*14.9*/(""".container """),format.raw/*14.20*/("""{"""),format.raw/*14.21*/("""
            """),format.raw/*15.13*/("""width: 90%;
            max-width: 1000px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            padding: 30px;
            margin: 50px auto;
        """),format.raw/*22.9*/("""}"""),format.raw/*22.10*/("""

        """),format.raw/*24.9*/("""h2 """),format.raw/*24.12*/("""{"""),format.raw/*24.13*/("""
            """),format.raw/*25.13*/("""font-size: 32px;
            color: #2e3a59;
            margin-bottom: 20px;
            text-align: center;
        """),format.raw/*29.9*/("""}"""),format.raw/*29.10*/("""

        """),format.raw/*31.9*/("""h3 """),format.raw/*31.12*/("""{"""),format.raw/*31.13*/("""
            """),format.raw/*32.13*/("""font-size: 24px;
            color: #4f5b74;
            margin-bottom: 15px;
            text-align: center;
        """),format.raw/*36.9*/("""}"""),format.raw/*36.10*/("""

        """),format.raw/*38.9*/("""table """),format.raw/*38.15*/("""{"""),format.raw/*38.16*/("""
            """),format.raw/*39.13*/("""width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            font-size: 16px;
            color: #555;
        """),format.raw/*44.9*/("""}"""),format.raw/*44.10*/("""

        """),format.raw/*46.9*/("""th, td """),format.raw/*46.16*/("""{"""),format.raw/*46.17*/("""
            """),format.raw/*47.13*/("""padding: 12px 15px;
            text-align: left;
        """),format.raw/*49.9*/("""}"""),format.raw/*49.10*/("""

        """),format.raw/*51.9*/("""th """),format.raw/*51.12*/("""{"""),format.raw/*51.13*/("""
            """),format.raw/*52.13*/("""background-color: #007bff;
            color: #fff;
            font-weight: bold;
        """),format.raw/*55.9*/("""}"""),format.raw/*55.10*/("""

        """),format.raw/*57.9*/("""td """),format.raw/*57.12*/("""{"""),format.raw/*57.13*/("""
            """),format.raw/*58.13*/("""background-color: #fafafa;
            border: 1px solid #e2e2e2;
        """),format.raw/*60.9*/("""}"""),format.raw/*60.10*/("""

        """),format.raw/*62.9*/("""tr:hover td """),format.raw/*62.21*/("""{"""),format.raw/*62.22*/("""
            """),format.raw/*63.13*/("""background-color: #f9f9f9;
            border-color: #d0d0d0;
        """),format.raw/*65.9*/("""}"""),format.raw/*65.10*/("""

        """),format.raw/*67.9*/(""".no-stats """),format.raw/*67.19*/("""{"""),format.raw/*67.20*/("""
            """),format.raw/*68.13*/("""color: #777;
            font-size: 18px;
            margin-top: 20px;
            text-align: center;
        """),format.raw/*72.9*/("""}"""),format.raw/*72.10*/("""

        """),format.raw/*74.9*/(""".btn """),format.raw/*74.14*/("""{"""),format.raw/*74.15*/("""
            """),format.raw/*75.13*/("""background-color: #007bff;
            color: white;
            padding: 12px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            margin-top: 30px;
            transition: background-color 0.3s;
            display: block;
            margin-left: auto;
            margin-right: auto;
        """),format.raw/*87.9*/("""}"""),format.raw/*87.10*/("""

        """),format.raw/*89.9*/(""".btn:hover """),format.raw/*89.20*/("""{"""),format.raw/*89.21*/("""
            """),format.raw/*90.13*/("""background-color: #0056b3;
        """),format.raw/*91.9*/("""}"""),format.raw/*91.10*/("""

        """),format.raw/*93.9*/("""footer """),format.raw/*93.16*/("""{"""),format.raw/*93.17*/("""
            """),format.raw/*94.13*/("""margin-top: 40px;
            text-align: center;
            font-size: 14px;
            color: #888;
        """),format.raw/*98.9*/("""}"""),format.raw/*98.10*/("""

        """),format.raw/*100.9*/("""footer a """),format.raw/*100.18*/("""{"""),format.raw/*100.19*/("""
            """),format.raw/*101.13*/("""color: #007bff;
            text-decoration: none;
        """),format.raw/*103.9*/("""}"""),format.raw/*103.10*/("""

        """),format.raw/*105.9*/("""footer a:hover """),format.raw/*105.24*/("""{"""),format.raw/*105.25*/("""
            """),format.raw/*106.13*/("""text-decoration: underline;
        """),format.raw/*107.9*/("""}"""),format.raw/*107.10*/("""
    """),format.raw/*108.5*/("""</style>

<div class="container">
    <h2>Word Statistics</h2>

    """),_display_(if(wordStats.getWordCount().isEmpty)/*113.42*/ {_display_(Seq[Any](format.raw/*113.44*/("""
    """),format.raw/*114.5*/("""<p class="no-stats">No word statistics available.</p>
    """)))}else/*115.12*/{_display_(Seq[Any](format.raw/*115.13*/("""
    """),format.raw/*116.5*/("""<h3>"""),_display_(/*116.10*/wordStats/*116.19*/.getSearch()),format.raw/*116.31*/("""</h3>
    <table>
        <thead>
        <tr>
            <th>Word</th>
            <th>Frequency</th>
        </tr>
        </thead>
        <tbody>
        """),_display_(/*125.10*/for((word, count) <- wordStats.getWordCount().toSeq.sortBy(-_._2)) yield /*125.76*/ {_display_(Seq[Any](format.raw/*125.78*/("""
        """),format.raw/*126.9*/("""<tr>
            <td>"""),_display_(/*127.18*/word),format.raw/*127.22*/("""</td>
            <td>"""),_display_(/*128.18*/count),format.raw/*128.23*/("""</td>
        </tr>
        """)))}),format.raw/*130.10*/("""
        """),format.raw/*131.9*/("""</tbody>
    </table>
    <button class="btn" onclick="window.print()">Print Statistics</button>
    """)))}),format.raw/*134.6*/("""
"""),format.raw/*135.1*/("""</div>

<footer>
    <p>Powered by <a href="#">Your Company</a> | <a href="#">Privacy Policy</a> | <a href="#">Terms of Service</a></p>
</footer>

<script>
        // Optional JavaScript for enhanced interactivity (if needed)
    </script>
""")))}),format.raw/*144.2*/("""
"""))
      }
    }
  }

  def render(wordStats:WordStatData): play.twirl.api.HtmlFormat.Appendable = apply(wordStats)

  def f:((WordStatData) => play.twirl.api.HtmlFormat.Appendable) = (wordStats) => apply(wordStats)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  SOURCE: app/views/wordStats.scala.html
                  HASH: 42f618d2ec221d2f358e06010f592e831463a134
                  MATRIX: 610->1|956->35|1076->60|1106->65|1132->83|1171->85|1199->87|1248->109|1276->110|1317->124|1530->310|1559->311|1598->323|1637->334|1666->335|1708->349|1974->588|2003->589|2042->601|2073->604|2102->605|2144->619|2293->741|2322->742|2361->754|2392->757|2421->758|2463->772|2612->894|2641->895|2680->907|2714->913|2743->914|2785->928|2961->1077|2990->1078|3029->1090|3064->1097|3093->1098|3135->1112|3222->1172|3251->1173|3290->1185|3321->1188|3350->1189|3392->1203|3513->1297|3542->1298|3581->1310|3612->1313|3641->1314|3683->1328|3786->1404|3815->1405|3854->1417|3894->1429|3923->1430|3965->1444|4064->1516|4093->1517|4132->1529|4170->1539|4199->1540|4241->1554|4384->1670|4413->1671|4452->1683|4485->1688|4514->1689|4556->1703|4972->2092|5001->2093|5040->2105|5079->2116|5108->2117|5150->2131|5213->2167|5242->2168|5281->2180|5316->2187|5345->2188|5387->2202|5530->2318|5559->2319|5599->2331|5637->2340|5667->2341|5710->2355|5799->2416|5829->2417|5869->2429|5913->2444|5943->2445|5986->2459|6051->2496|6081->2497|6115->2503|6253->2613|6294->2615|6328->2621|6412->2687|6452->2688|6486->2694|6519->2699|6538->2708|6572->2720|6769->2889|6852->2955|6893->2957|6931->2967|6982->2990|7008->2994|7060->3018|7087->3023|7150->3054|7188->3064|7324->3169|7354->3171|7635->3421
                  LINES: 23->1|28->2|33->2|35->4|35->4|35->4|36->5|37->6|37->6|38->7|43->12|43->12|45->14|45->14|45->14|46->15|53->22|53->22|55->24|55->24|55->24|56->25|60->29|60->29|62->31|62->31|62->31|63->32|67->36|67->36|69->38|69->38|69->38|70->39|75->44|75->44|77->46|77->46|77->46|78->47|80->49|80->49|82->51|82->51|82->51|83->52|86->55|86->55|88->57|88->57|88->57|89->58|91->60|91->60|93->62|93->62|93->62|94->63|96->65|96->65|98->67|98->67|98->67|99->68|103->72|103->72|105->74|105->74|105->74|106->75|118->87|118->87|120->89|120->89|120->89|121->90|122->91|122->91|124->93|124->93|124->93|125->94|129->98|129->98|131->100|131->100|131->100|132->101|134->103|134->103|136->105|136->105|136->105|137->106|138->107|138->107|139->108|144->113|144->113|145->114|146->115|146->115|147->116|147->116|147->116|147->116|156->125|156->125|156->125|157->126|158->127|158->127|159->128|159->128|161->130|162->131|165->134|166->135|175->144
                  -- GENERATED --
              */
          