import org.specs2._
import org.specs2.matcher.MatchResult


class FlightsExplorerSpec extends Specification {def is = s2"""

    all flights should be read from file                                        $flightsAmount
    amount of arrivals should be calculated properly                            $arrivalsMap
    amount of starts should be calculated properly                              $startsMap
    amount of diffs in arrivals and starts should be calculated properly        $diffsMap
    amount of arrivals for week should be calculated properly                   $arrivalsForWeek
    and saved separately for each week                                          $arrivalsWeeklyMap
"""

  val flightsExplorer = FlightsExplorer
  val flights: Seq[Flight] = flightsExplorer.getFlights

  def flightsAmount: MatchResult[Any] = {
    flights.size
  } must_== 471949

  def arrivalsMap: MatchResult[Map[String, Int]] = {

    flightsExplorer.getAmountOfArrivalsToAirports(flights)

  } must havePairs("ABE" -> 137, "IAH" -> 14569, "XNA" -> 831, "YUM" -> 264)

  def startsMap: MatchResult[Map[String, Int]] = {

    flightsExplorer.getAmountOfStartsFromAirports(flights)

  } must havePairs("ABE" -> 135, "IAH" -> 14581, "XNA" -> 826, "YUM" -> 264)

  def diffsMap: MatchResult[Map[String, Int]] = {

    val arrivals = flightsExplorer.getAmountOfArrivalsToAirports(flights)
    val starts = flightsExplorer.getAmountOfStartsFromAirports(flights)
    flightsExplorer.getDiffsBetweenArrivalsAndStarts(arrivals, starts)

  } must havePairs("ABE" -> 2, "IAH" -> -12, "XNA" -> 5)

  def arrivalsForWeek: MatchResult[Map[String, Int]] = {

    flightsExplorer.getArrivalsForWeek(1, flights)

  } must havePairs("ABE" -> 10, "IAH" -> 2493, "XNA" -> 118, "YUM" -> 52)

  def arrivalsWeeklyMap: MatchResult[Map[Int, Map[String, Int]]] = {

    flightsExplorer.getAmountOfArrivalsToAirportsWeekly(flights)

  } must haveKeys(1, 2, 3, 4, 5)

}