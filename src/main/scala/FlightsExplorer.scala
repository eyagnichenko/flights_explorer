import java.io.FileNotFoundException
import java.time.format.DateTimeParseException

import utils.OutputWriter


/**
  * Implementation of Flights Explorer responsible for gathering information from [[Seq]] of [[Flight]]
  * extracted from [[FlightsReader]].
  */
object FlightsExplorer extends App {

  println("Read flights...\n")
  val flights = getFlights

  println("Get arrivals to airports...")  // task #1
  val arrivalsMap: Map[String, Int] = getAmountOfArrivalsToAirports(flights, toFile = true)

  println("Get starts from airports...")
  val startsMap: Map[String, Int] = getAmountOfStartsFromAirports(flights)

  println("Get difference in arrivals and starts from airports...")  // task #2
  val diffs: Map[String, Int] = getDiffsBetweenArrivalsAndStarts(arrivalsMap, startsMap, toFile = true)

  println("Get arrivals to airports weekly...")  // task #3
  val arrivalsWeeklyMap: Map[Int, Map[String, Int]] = getAmountOfArrivalsToAirportsWeekly(flights, toFile = true)

  println("Done.")

  /**
    * Takes flights from file using [[FlightsReader]].
    * Handles 'file not found' exceptions and errors in source file.
    * @return sequence containing flights.
   */
  def getFlights: Seq [Flight] = {

    var flights = Seq[Flight]()

    //    try flights = new FlightsReader().readFlights()
    try flights = new FlightsReader().readFlightsFromGzip()
    catch {
      case _: FileNotFoundException => System.err.println("File not found.")  // for readFlightsFromGzip method
//      case _: NullPointerException => System.err.println("File not found.")  // for readFlights
      case _: NumberFormatException => System.err.println("Couldn't init flights from source file.")
      case _: DateTimeParseException => System.err.println("Couldn't init flights from source file.")
    }
    if (flights.isEmpty) System.exit(1) // exit if failed to init flights
    flights

  }

  /** Task #1.
    * Calculates amount of arrivals to each airport.
    * @param flights: [[Seq]] containing flights data.
    * @param toFile: [[Boolean]] used to save data to file in case of need.
    * @return map containing airports with amount of arrivals to them.
    */
  def getAmountOfArrivalsToAirports(flights: Seq[Flight], toFile: Boolean = false): Map[String, Int] = {

    val arrivals: Seq[String] = for (f <- flights) yield f.dest
    var resMap = Map[String, Int]()
    for (a <- arrivals.distinct) resMap += a -> arrivals.count(_==a)
    if (toFile) new OutputWriter().writeToFile(resMap, "output/task1_arrivals.txt")
    resMap

  }

  /**
    * Calculates amount of starts from each airport.
    * @param flights: [[Seq]] containing flights data.
    * @param toFile: used to save data to file in case of need.
    * @return map containing airports with amount of starts from them.
    */
  def getAmountOfStartsFromAirports(flights: Seq[Flight], toFile: Boolean = false): Map[String, Int] = {

    val starts: Seq[String] = for (f <- flights) yield f.origin
    var resMap = Map[String, Int]()
    for (s <- starts.distinct) resMap += s -> starts.count(_==s)
    if (toFile) new OutputWriter().writeToFile(resMap, "output/task1_arrivals.txt")
    resMap

  }

  /** Task #2.
    * Calculates the difference in arrivals and starts from each airport.
    * @param arrivalsMap: map of airports with amount of arrivals to them.
    * @param startsMap: map of airports with amount of starts from them.
    * @param toFile: used to save data to file in case of need.
    * @return map containing airports with non-zero difference value in arrivals and starts from them.
    */
  def getDiffsBetweenArrivalsAndStarts(arrivalsMap: Map[String, Int], startsMap: Map[String, Int],
                                       toFile: Boolean = false): Map[String, Int] = {

    var resMap = Map[String, Int]()
    resMap = startsMap ++ arrivalsMap.map{
      case (k, v) => k -> (v - startsMap.getOrElse(k,0))  // get differences between arrivals and starts from airports
    }
    resMap = resMap.filter((t) => t._2 != 0) // remove zero values
    if (toFile) new OutputWriter().writeToFile(resMap, "output/task2_diffs.txt")
    resMap

  }

  /**
    * Calculates amount of arrivals for separate week.
    * @param week: [[Int]] containing week number.
    * @param flights: [[Seq]] containing flights data.
    * @param toFile: used to save data to file in case of need.
    * @return map containing airports with amount of arrivals to them for separate week.
    */
  def getArrivalsForWeek(week: Int, flights: Seq[Flight], toFile: Boolean = false): Map[String, Int] = {

    val arrivals = for (f <- flights if f.weekOfYear == week) yield f.dest
    var resMap = Map[String, Int]()
    for (a <- arrivals.distinct) resMap += a -> arrivals.count(_==a)

    var append: Boolean  = true
    if (week == 1) append = false  // re-write file for the 1st week, but add values to file for next weeks
    if (toFile) new OutputWriter().writeToFile(resMap, week, "output/task3_arrivals_weekly.txt", append = append)

    resMap

  }

  /** Task #3.
    * Calculates amount of arrivals to airports weekly.
    * @param flights: [[Seq]] containing flights data.
    * @param toFile: used to save data to file in case of need.
    * @return map containing airports with amount of arrivals to them weekly.
    */
  def getAmountOfArrivalsToAirportsWeekly(flights: Seq[Flight], toFile: Boolean = false): Map[Int, Map[String, Int]] = {

    val weeks = for (f <- flights) yield f.weekOfYear
    var resMap = Map[Int, Map[String, Int]]()
    for (w <- weeks.distinct) resMap += w -> getArrivalsForWeek(w, flights, toFile)
    resMap

  }

}