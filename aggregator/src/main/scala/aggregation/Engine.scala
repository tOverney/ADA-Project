package aggregation

import com.github.nscala_time.time.Imports._
import com.github.tototoshi.csv.CSVReader
import spray.json._
import spray.json.DefaultJsonProtocol._
import java.io.File
import scala.collection.mutable.{Map => MuMap}
import scala.collection.immutable.{Map => ImmuMap}

import utils.DateUtils._

case class Engine(interval: Int) {
  val validTimeSpan: ValidDateTimeSpan = fetchAvailableData()
  val cachedData: MuMap[String, List[Itinerary]] = MuMap()

  def fetchAvailableData(): ValidDateTimeSpan = {
    val dataDir = new File(s"../data")

    if (!dataDir.exists() || !dataDir.isDirectory()) {
      throw new IllegalStateException("No data folder present!" + 
          " Make sure you have it and non empty!")
    }

    // This ensure us that .DS_STORE or other files are not taken into account
    val allData = dataDir.list.withFilter(
        _.startsWith("2017")).map(_.split("_")(0))

    if (allData.isEmpty) {
      throw new IllegalStateException("No data available! Make sure the" +
          " `data` folder is non empty!")
    }

    val allDates = allData.map(date => DateTime.parse(date, formatterDateOnly))

    // Find min and max dates that can be queried
    val firstDate :: otherDates = allDates.toList
    val (minDate, maxDayDate) = otherDates.foldLeft((firstDate, firstDate)){
      case ((min, max), curr) =>
        (if (curr < min) curr else min, if (curr > max) curr else max)
    }
    val maxDate = maxDayDate.lastStartIntervalOfDay(interval)

    ValidDateTimeSpan(minDate, maxDate, interval)
  }

  def aggregate(startDate: DateTime): JsValue = {
    assert(validTimeSpan.dateIsInTimeSpan(startDate))
    println("Result not yet computed, needs to compute")

    val dateToLoad = startDate.toLocalDate().toString()

    val itineraries =
        cachedData.getOrElse(dateToLoad, computeCacheValue(dateToLoad))

    val res = aggregateItineraries(itineraries, startDate)

    res.toJson
  }

  def computeCacheValue(dateToLoad: String): List[Itinerary] = {
      val stops = loadDataFile(dateToLoad)
      val itineraries = computeItineraries(stops)
      cachedData += (dateToLoad -> itineraries)
      itineraries
  }

  def loadDataFile(dateToLoad: String): List[Stop] = {
    println("Data for day not loaded!")
    val fileName = "../data/" + dateToLoad + "_out.csv"
    // we need to drop the header
    CSVReader.open(new File(fileName)).all().drop(1).map(row => Stop(row))
  }

  def computeItineraries(stops: List[Stop]): List[Itinerary] = {
    val allItineraries = for {
      tripUnsorted <- stops.groupBy(s => s.tripId).values.toList
    } yield {
      val start :: trip = tripUnsorted.sortBy(_.departureTime)
      val (itineraries, _)  = trip.foldLeft(List[Itinerary](), start){
        case ((acc, beg), end) =>
          (acc :+ Itinerary(beg, end), end)
      }
      itineraries
    }
    allItineraries.flatten.filterNot(_.path.isEmpty)
  }

  def aggregateItineraries(itineraries: List[Itinerary],
      intStart: DateTime): ImmuMap[String, Int] = {
    val res: MuMap[String, Int] = MuMap()

    itineraries.foreach { i =>
        filterSegments(intStart, i).foreach { seg =>
          val newAmount = res.getOrElse(seg, 0) + i.passengerAmount
          res += (seg -> newAmount)
        }
    }
    res.toMap
  }

  def filterSegments(intStart: DateTime, iti: Itinerary): List[String] = {
    val Itinerary(start, stop, path, amnt) = iti
    val intEnd = intStart + interval.minutes
    if ((intStart < stop && intEnd > start) || path.isEmpty) {
      Nil
    } else if (intStart < start && intEnd > stop) {
      path
    } else {
      val routeTime: Int = stop.minutesOfDay - start.minutesOfDay
      val safeRouteTime = if (routeTime == 0) 1 else routeTime
      val proportionToTake = path.length * interval / safeRouteTime

      // some of the itinerary is not in that time interval
      if (intStart > start) {
        path.takeRight(proportionToTake)
      } else {
        path.take(proportionToTake)
      }
    }
  }
}
