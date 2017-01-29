package aggregation

import com.github.nscala_time.time.Imports._

case class Itinerary(startTime: DateTime, endTime: DateTime, path: List[String],
    passengerAmount: Int)

object Itinerary {
  def apply(beginning: Stop, ending: Stop): Itinerary = {
    Itinerary(beginning.departureTime, ending.arrivalTime, beginning.path,
        beginning.passengerAmount)
  }
}
