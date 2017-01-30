package utils

import com.github.nscala_time.time.Imports._

package object DateUtils {
  implicit class BoundedDateTime(self: DateTime) {
    def isAcceptableDate(implicit boundaries: ValidDateTimeSpan): Boolean = {
      import boundaries._
      dateIsIncrementOfInterval(self) && dateIsInTimeSpan(self)
    }

    def lastStartIntervalOfDay(interval: Int): DateTime = {
      self + (minutesInADay - interval).minutes
    }

    def nextBoundary(interval: Int): DateTime = {
      self + interval.minutes
    }
  }

  implicit class PrettyPrintableDateTime(self: DateTime) {
    def prettyString(): String = self.toString().dropRight(13)
  }

  case class ValidDateTimeSpan(dateMin: DateTime,
      dateMax: DateTime, interval: Int) {

    def dateIsInTimeSpan(date: DateTime): Boolean = {
      dateMin <= date && dateMax >= date
    }

    def dateIsIncrementOfInterval(date: DateTime): Boolean = {
      val minutesOfDay: Int = date.millisOfDay().get() / 60000
      minutesOfDay % interval == 0
    }
  }

  private val minutesInADay = 24 * 60
  /**
   * Limits the interval values to a divisor of 24hours.
   */
  def isValidInterval(interval: Int): Boolean = {
    minutesInADay % interval == 0
  }

  private val dateAndTimeFormat = "yyyy-MM-dd'T'HH:mm"
  val formatter = DateTimeFormat.forPattern(dateAndTimeFormat)

  private val dateFormat = "yyy-MM-dd"
  val formatterDateOnly = DateTimeFormat.forPattern(dateFormat)
}
