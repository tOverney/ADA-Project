package aggregation

import com.github.nscala_time.time.Imports._
import com.github.tototoshi.csv.CSVReader
import spray.json._
import java.io.File
import scala.collection.mutable.Map

import utils.DateUtils._

case class Engine(interval: Int) {
  val validTimeSpan: ValidDateTimeSpan = fetchAvailableData()
  val cachedData: Map[String, List[Stop]] = Map()

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

    // find min date
    val firstDate :: otherDates = allDates.toList
    val (minDate, maxDayDate) = otherDates.foldLeft((firstDate, firstDate)){
      case ((min, max), curr) =>
        (if (curr < min) curr else min, if (curr > max) curr else max)
    }

    val maxDate = maxDayDate.lastStartIntervalOfDay(interval)
    // TODO compute max date correctly
    ValidDateTimeSpan(minDate, maxDate, interval)
  }

  def aggregate(startDate: DateTime): JsValue = {
    assert(validTimeSpan.dateIsInTimeSpan(startDate))
    println("Result not yet computed, needs to compute")

    val dateToLoad = startDate.toLocalDate().toString()

    cachedData.getOrElse(dateToLoad, loadDataFile(dateToLoad))
    JsTrue
  }

  def loadDataFile(dateToLoad: String): List[Stop] = {
    val fileName = "../data/" + dateToLoad + "_out.csv"
    val allRawStops = CSVReader.open(new File(fileName)).all()

    allRawStops.map(stop => Stop(stop))
  }
}
