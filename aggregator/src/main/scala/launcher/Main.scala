package launcher

import com.github.nscala_time.time.Imports._
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpEntity, ContentTypes, ContentType, StatusCodes, MediaTypes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.headers.HttpOriginRange
import akka.stream.ActorMaterializer
import akka.util.Timeout
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._

import com.typesafe.config.ConfigFactory
import scala.io.StdIn
import ch.megard.akka.http.cors.CorsDirectives._
import ch.megard.akka.http.cors._

import java.io.File
import org.apache.commons.io.FileUtils

import caching.CacheHandler
import utils.DateUtils._

object Main extends App {
  val interval = 15
  if (!isValidInterval(interval)) {
    throw new IllegalArgumentException(
        "Your interval value must be a divisor of 1440 (minutes in a day)")
  }
  println(s"Time interval used by the server: $interval minutes")

  val cacheHandler = CacheHandler(interval)
  cacheHandler.loadCache()
  implicit val validTS: ValidDateTimeSpan = cacheHandler.engine.validTimeSpan

  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("ada-aggregator")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val host = "localhost"
  val port = 8080
  val DateRegex = """\d{4}-\d{2}-\d{2}T\d{2}:\d{2}""".r

  // Static files that need to be served to avoid CORS error
  val edgesJsonFile = new File("../app/edges.json")
  val edgesJson = FileUtils.readFileToString(edgesJsonFile,
      java.nio.charset.StandardCharsets.UTF_8)

  val switzerlandJsonFile = new File("../app/switzerland.json")
  val switzerlandJson = FileUtils.readFileToString(switzerlandJsonFile,
      java.nio.charset.StandardCharsets.UTF_8)

  val indexFile = new File("../app/index.html")
  val indexHtml = FileUtils.readFileToString(indexFile,
      java.nio.charset.StandardCharsets.UTF_8)

  val corsSettings = CorsSettings.defaultSettings.copy(
    allowCredentials = false,
    allowedOrigins = HttpOriginRange.*
  )

  val api = cors(corsSettings) {
    get {
      path("") {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, indexHtml))
      }
    } ~ get {
      pathPrefix("occupancy" / IntNumber / DateRegex) {
        (reqInterval, stringDate) =>
          try {
            if (interval != reqInterval) {
              println("ERROR: Got a request for a different time interval than "
                  + interval)
              complete(StatusCodes.BadRequest)
            } else {
              val time = DateTime.parse(stringDate, formatter)
              if (time.isAcceptableDate) {
                val startTime = System.currentTimeMillis

                val res = getRequestedResult(time)

                val elapsedTime = System.currentTimeMillis - startTime
                val sec = elapsedTime / 1000D
                println(s"REQ `${time.prettyString}` handled in $sec s")

                complete(HttpEntity(ContentTypes.`application/json`, res))
              } else {
                complete(StatusCodes.BadRequest)
              }
            }
          // In case of something going wrong with the request, the server stays up!
          } catch {
            case e: Throwable =>
              println("Something went wrong! Probably because the date requested is not valid")
              e.printStackTrace()
              complete(StatusCodes.BadRequest)
          }
      }
    } ~ get {
      pathPrefix("edges") {
        complete(HttpEntity(ContentTypes.`application/json`, edgesJson))
      }
    } ~ get { 
      pathPrefix("switzerland") {
        complete(HttpEntity(ContentTypes.`application/json`, switzerlandJson))
      }
    }
  }

  def getRequestedResult(time: DateTime): String = {
    val fetchResult = cacheHandler.fetch(time)
    fetchResult.result.prettyPrint
  }


  // start a new HTTP server on port 8080 with our service actor as the handler
  val serverFuture = Http().bindAndHandle(api, host, port)


  // // testing
  // val time = DateTime.parse("2017-01-30T07:00", formatter)
  // if (time.isAcceptableDate) {
  //   getRequestedResult(time)
  // } else {
  //   println("ERROR: Date is not in range")
  // }

  // Shutting down the server on <Enter> pressed
  StdIn.readLine()
  serverFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
}
