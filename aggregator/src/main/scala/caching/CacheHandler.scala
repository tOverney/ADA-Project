package caching

import com.github.nscala_time.time.Imports._
import spray.json._
import java.io.File
import org.apache.commons.io.FileUtils
import scala.collection.mutable.Map

import aggregation.Engine
import utils.DateUtils

case class CacheHandler(interval: Int) {
  val cache: Map[DateTime, CachedResult] = Map()
  val engine: Engine = Engine(interval)

  def loadCache(): Unit = {
    val intervalDir = new File(s"cache/$interval")
    if (!intervalDir.exists()) {
      intervalDir.mkdir()
    }

    for (
      dayDir <- intervalDir.listFiles;
      jsonFile <- dayDir.listFiles
    ) {
      val stringDate = dayDir.getName() + "T" + jsonFile.getName().split('.')(0)
      val key = DateTime.parse(stringDate, DateUtils.formatter)
      val result = FileUtils.readFileToString(jsonFile,
          java.nio.charset.StandardCharsets.UTF_8).parseJson
      val value = CachedResult(key, interval, result)
      cache += ((key, value))
    }
  }

  def fetch(date: DateTime): CachedResult = {
    cache.getOrElse(date, computeResult(date))
  }

  def printCache(): Unit = {
    println("--- Cache entries ---")
    for (entry <- cache.toList) {
      println(entry._1.toString() + " -> " + entry._2.result.prettyPrint)
    }
    println("---------------------")
  }

  private def computeResult(date: DateTime): CachedResult = {
    // we create the folder that could be needed
    val dateDir = new File(s"cache/$interval/" + date.toString().split('T')(0))
    if (!dateDir.exists()) {
      dateDir.mkdir()
    }

    val result = engine.aggregate(date)
    val res = CachedResult(date, interval, result)
    cache += ((date, res))
    res.writeOut()

    res
  }
}
