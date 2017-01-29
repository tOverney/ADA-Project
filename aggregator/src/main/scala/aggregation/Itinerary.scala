package aggregation

import com.github.nscala_time.time.Imports._
import scala.collection.immutable.Map
import scala.util.Random
import spray.json._
import spray.json.DefaultJsonProtocol._

import utils.DateUtils.formatterDateOnly

case class Stop(tripId: String, stationName: String, trainName: String,
    arrivalTime: DateTime, departureTime: DateTime, passengerAmnt: Int,
    path: List[Int])

object Stop {
  private val possibleValues: Map[String, Double] = Map(
    "" -> 0D,
    "-1" -> 0D,
    "1" -> 1D/3,
    "2" -> 2D/3,
    "3" -> 1D
  ).withDefault(_ => Double.NaN)

  def computePassengerAmnt(capacity1: Int, capacity2: Int,
      class1Occ: String, class2Occ: String): Int = {
    val class1 = possibleValues(class1Occ)
    val class2 = possibleValues(class2Occ)

    assert(!class2.isNaN && !class1.isNaN)

    (capacity1 * class1 + capacity2 * class2).toInt
  }

  def apply(row: List[String]): Stop = {
    assert(row.length == 21)

    val date = DateTime.parse(row(2), formatterDateOnly)
    val trainName = row(5)
    val tripId = row(6)
    val arrivalTime = date + (row(10).toInt.minutes)
    val departureTime = date + (row(11).toInt.minutes)
    val stationName = row(15)
    val path = row(17).parseJson.convertTo[List[Int]]
    // TODO have real capacities
    val capacity1 = 200 + Random.nextInt(500)
    val capacity2 = 7000 + Random.nextInt(2000)
    val class1Occ = row(19)
    val class2Occ = row(20)

    val passengerAmnt = computePassengerAmnt(capacity1, capacity2, class1Occ,
        class2Occ)

    Stop(tripId, stationName, trainName, arrivalTime, departureTime,
        passengerAmnt, path)
  }
}
