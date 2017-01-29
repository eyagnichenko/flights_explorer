import org.specs2._
import org.specs2.matcher.MatchResult


class FlightsReaderSpec extends Specification {def is = s2"""
  Week of year should be calculated properly for:
    the first day of week                                     $weekForFirstDayOfWeek
    the day in the middle of week                             $weekForDayInTheMiddleOfWeek
    the last day of week                                      $weekForLastDayOfWeek
"""

  val flightsReader: FlightsReader = new FlightsReader

  def weekForFirstDayOfWeek: MatchResult[Any] = {

    flightsReader.getWeekOfYear("2014-01-27")  // case for Monday of the fifth week

  } must_==5

  def weekForDayInTheMiddleOfWeek: MatchResult[Any] = {

    flightsReader.getWeekOfYear("2014-01-01")  // case for Wednesday of the first week

  } must_==1

  def weekForLastDayOfWeek: MatchResult[Any] = {

    flightsReader.getWeekOfYear("2014-01-19")  // case for Sunday of the third week

  } must_==3

}