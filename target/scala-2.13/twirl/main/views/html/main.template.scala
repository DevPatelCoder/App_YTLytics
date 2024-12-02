
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

object main extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template2[String,Html,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(title: String)(content: Html):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.32*/("""

"""),format.raw/*3.1*/("""<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>"""),_display_(/*7.13*/title),format.raw/*7.18*/("""</title>
    <link rel="stylesheet" href=""""),_display_(/*8.35*/routes/*8.41*/.Assets.versioned("stylesheets/main.css")),format.raw/*8.82*/("""">
    <link rel="script" href=""""),_display_(/*9.31*/routes/*9.37*/.Assets.versioned("javascipts/main.js")),format.raw/*9.76*/("""">

<!--    <link rel="icon" href=""""),_display_(/*11.33*/routes/*11.39*/.Assets.versioned("images/img.png")),format.raw/*11.74*/("""" type="image/png">-->


</head>
<body>
<div class="container">
    """),_display_(/*17.6*/content),format.raw/*17.13*/("""
"""),format.raw/*18.1*/("""</div>
    <script src='"""),_display_(/*19.19*/routes/*19.25*/.Assets.versioned("javascripts/main.js")),format.raw/*19.65*/("""' type="text/javascript"></script>
</body>
</html>
"""))
      }
    }
  }

  def render(title:String,content:Html): play.twirl.api.HtmlFormat.Appendable = apply(title)(content)

  def f:((String) => (Html) => play.twirl.api.HtmlFormat.Appendable) = (title) => (content) => apply(title)(content)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  SOURCE: app/views/main.scala.html
                  HASH: c7ac7fc4fe5c998f7c1ec96ac039dad26d4d3c3f
                  MATRIX: 911->1|1036->31|1066->35|1175->118|1200->123|1270->167|1284->173|1345->214|1405->248|1419->254|1478->293|1543->331|1558->337|1614->372|1715->447|1743->454|1772->456|1825->482|1840->488|1901->528
                  LINES: 27->1|32->1|34->3|38->7|38->7|39->8|39->8|39->8|40->9|40->9|40->9|42->11|42->11|42->11|48->17|48->17|49->18|50->19|50->19|50->19
                  -- GENERATED --
              */
          