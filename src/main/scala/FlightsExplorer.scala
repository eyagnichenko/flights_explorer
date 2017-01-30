import java.io.FileNotFoundException
import java.nio.charset.MalformedInputException
import java.time.format.DateTimeParseException

import utils.OutputWriter


/**
  * Implementation of Flights Explorer responsible for gathering information from [[Seq]] of [[Flight]]
  * extracted from [[FlightsReader]].
  */
object FlightsExplorer extends App {

  val writer: OutputWriter = new OutputWriter

  println("Read flights...\n")
  val flights: Seq[Flight] = getFlights()

  println("Get arrivals to airports...")
  val arrivalsMap: Map[String, Int] = getAmountOfArrivalsToAirports(flights)  // task #1
  writer.writeToFile(arrivalsMap,  "output/task1_arrivals.txt")

  println("Get starts from airports...")
  val startsMap: Map[String, Int] = getAmountOfStartsFromAirports(flights)

  println("Get difference in arrivals and starts from airports...")
  val diffsMap: Map[String, Int] = getDiffsBetweenArrivalsAndStarts(arrivalsMap, startsMap)  // task #2
  writer.writeToFile(diffsMap, "output/task2_diffs.txt")

  println("Get arrivals to airports weekly...")
  val arrivalsWeeklyMap: Map[Int, Map[String, Int]] = getAmountOfArrivalsToAirportsWeekly(flights)  // task #3
  writer.writeToFileWeekly(arrivalsWeeklyMap, "output/task3_arrivals_weekly.txt")

  println("Done.")

  /**
    * Takes flights from file using [[FlightsReader]].
    * Handles 'out of memory' error, 'file not found', 'malformed input' exceptions and errors in source file.
    * @return [[Seq]] of [[Flight]].
   */
  def getFlights(sourceFile: String = "src/main/resources/planes_log.csv.gz"): Seq [Flight] = {

    var flights = Seq[Flight]()
    try flights = new FlightsReader().readFlightsFromGzip(sourceFile)
    catch {
      case _: FileNotFoundException => System.err.println("File not found.")
      case _: MalformedInputException => System.err.println("Wrong file format.")
      case _: OutOfMemoryError => System.err.println("Not enough memory. Try to run sbt with '-mem' parameter.\n" +
        "Approximately 512MB are needed to process data for a month and 5GB are needed to process data for a year.")
      case _: NumberFormatException => System.err.println("Failed to init flights from source file.")
      case _: DateTimeParseException => System.err.println("Failed to init flights from source file.")
    }
    if (flights.isEmpty) System.exit(1) // exit if failed to init flights
    flights

  }

  /** Task #1.
    * Calculates amount of arrivals to each airport.
    * @param flights: [[Seq]] containing flights data.
    * @return map containing airports with amount of arrivals to them.
    */
  def getAmountOfArrivalsToAirports(flights: Seq[Flight]): Map[String, Int] = {

    val arrivals: Seq[String] = for (f <- flights) yield f.dest
    var resMap = Map[String, Int]()
    for (a <- arrivals.distinct) resMap += a -> arrivals.count(_==a)
    resMap
  }

  /**
    * Calculates amount of starts from each airport.
    * @param flights: [[Seq]] containing flights data.
    * @return map containing airports with amount of starts from them.
    */
  def getAmountOfStartsFromAirports(flights: Seq[Flight]): Map[String, Int] = {

    val starts: Seq[String] = for (f <- flights) yield f.origin
    var resMap = Map[String, Int]()
    for (s <- starts.distinct) resMap += s -> starts.count(_==s)
    resMap

  }

  /** Task #2.
    * Calculates the difference in arrivals and starts from each airport.
    * @param arrivalsMap: map of airports with amount of arrivals to them.
    * @param startsMap: map of airports with amount of starts from them.
    * @return map containing airports with non-zero difference value in arrivals and starts from them.
    */
  def getDiffsBetweenArrivalsAndStarts(arrivalsMap: Map[String, Int], startsMap: Map[String, Int]): Map[String, Int] = {

    var resMap = Map[String, Int]()
    resMap = startsMap ++ arrivalsMap.map{
      case (k, v) => k -> (v - startsMap.getOrElse(k,0))  // get differences between arrivals and starts from airports
    }
    resMap.filter((t) => t._2 != 0) // remove zero values

  }

  /**
    * Calculates amount of arrivals for separate week.
    * @param week: [[Int]] containing week number.
    * @param flights: [[Seq]] containing flights data.
    * @return map containing airports with amount of arrivals to them for separate week.
    */
  def getArrivalsForWeek(week: Int, flights: Seq[Flight]): Map[String, Int] = {

    val arrivals = for (f <- flights if f.weekOfYear == week) yield f.dest
    var resMap = Map[String, Int]()
    for (a <- arrivals.distinct) resMap += a -> arrivals.count(_==a)
    resMap

  }

  /** Task #3.
    * Calculates amount of arrivals to airports weekly.
    * @param flights: [[Seq]] containing flights data.
    * @return map containing airports with amount of arrivals to them weekly.
    */
  def getAmountOfArrivalsToAirportsWeekly(flights: Seq[Flight]): Map[Int, Map[String, Int]] = {

    val weeks = for (f <- flights) yield f.weekOfYear
    var resMap = Map[Int, Map[String, Int]]()
    for (w <- weeks.distinct) resMap += w -> getArrivalsForWeek(w, flights)
    resMap

  }

}