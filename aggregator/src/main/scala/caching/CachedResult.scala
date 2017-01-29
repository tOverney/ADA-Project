package caching

import com.github.nscala_time.time.Imports._
import spray.json.{JsValue, JsTrue}
import java.io.PrintWriter

case class CachedResult(date: DateTime, interval: Int, result: JsValue) {
  def writeOut(): Unit = {

    // we ditch the part after the minutes
    val Array(yyMMdd, time) = date.toString().split('T')
    val hourMin = time.substring(0, 5)

    val printer = new PrintWriter(s"cache/$interval/$yyMMdd/$hourMin.json")
    try {
      printer.println(result.prettyPrint)
    } finally {
      printer.close()
    }
  }
}
