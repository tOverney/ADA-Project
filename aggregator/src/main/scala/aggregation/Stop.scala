package aggregation

import com.github.nscala_time.time.Imports._
import scala.collection.immutable.Map
import spray.json._
import spray.json.DefaultJsonProtocol._

import utils.DateUtils.formatterDateOnly
import utils.TrainTypes._

case class Stop(tripId: String, stationName: String, trainType: TrainType,
    arrivalTime: DateTime, departureTime: DateTime, passengerAmount: Int,
    path: List[String])

object Stop {

  private def parsePath(rawPath: String): List[String] = {
    val emptyPath = Set("None", "[]", "")
    if (emptyPath(rawPath)) {
      List()
    } else {
      rawPath.drop(1).dropRight(1).split(", ").toList
    }
  }

  def apply(row: List[String]): Stop = {
    // we have a lot information about the content of a row as we generate it
    assert(row.length == 21)

    val date = DateTime.parse(row(2), formatterDateOnly)
    val trainType = TrainType.fromLongName(row(5))
    val tripId = row(6)
    val arrivalTime = date + (row(10).toInt.seconds)
    val departureTime = date + (row(11).toInt.seconds)
    val stationName = row(15)
    val path = parsePath(row(17))
    val class1Occ = row(19)
    val class2Occ = row(20)

    val passengerAmnt = trainType.computePassengerAmnt(class1Occ, class2Occ)

    Stop(tripId, stationName, trainType, arrivalTime, departureTime,
        passengerAmnt, path)
  }
}
