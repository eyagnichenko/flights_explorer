import java.io.{BufferedInputStream, FileInputStream}
import java.time.{LocalDate, DayOfWeek}
import java.time.format.DateTimeFormatter
import java.time.temporal.{TemporalField, WeekFields}
import java.util.zip.GZIPInputStream

import scala.io.Source


/**
  * Implementation of FlightsReader responsible for reading flights from file.
  */
class FlightsReader() {

  // CSV file mapping
  val Year = 0
  val Quarter = 1
  val Month = 2
  val DayOfMonth = 3
  val DayOfTheWeek = 4
  val FlightDate = 5
  val Origin = 6
  val Dest = 7

  val gzipped: String = "src/main/resources/planes_log.csv.gz"
  /**
    * Read flights from gzipped file.
    * @param file: path to gzipped file with source data.
    * @return [[Seq]] containing flights.
    */
  def readFlightsFromGzip(file: String = gzipped): Seq[Flight] = {

    for {
      line <- Source.fromInputStream(
        new GZIPInputStream(
          new BufferedInputStream(new FileInputStream(file)))).getLines()
        .drop(1) // ignore the first line of CSV file that contains header
        .toVector
      values = line.split(",").map(_.trim)
    } yield Flight(values(Year).toInt, values(Quarter).toInt, values(Month).toInt, values(DayOfMonth),
      values(DayOfTheWeek),
      getWeekOfYear(values(FlightDate)),  // calculate weekOfYear value for task #3
      values(FlightDate), values(Origin).replace("\"", ""), values(Dest).replace("\"", ""))

  }

  val src: String = "planes_log.csv"  // please, gunzip file in 'src/main/resources'
  /**
    * Read flights from gunzipped CSV file.
    * @param file: CSV file with source data.
    * @return [[Seq]] containing flights.
    */
  def readFlights(file: String = src): Seq[Flight] = {

    for {
      line <- Source.fromResource(file).getLines.drop(1).toVector
      values = line.split(",").map(_.trim)
    } yield Flight(values(Year).toInt, values(Quarter).toInt, values(Month).toInt, values(DayOfMonth),
      values(DayOfTheWeek),
      getWeekOfYear(values(FlightDate)),  // calculate weekOfYear value for task #3
      values(FlightDate), values(Origin).replace("\"", ""), values(Dest).replace("\"", ""))

  }

  /**
    * Calculates week of year from the date string.
    * @param dateStr: date string.
    * @return [[Int]] containing week of year value.
    */
  def getWeekOfYear(dateStr: String): Int = {

    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date: LocalDate = LocalDate.parse(dateStr, formatter)
    val weekOfYear: TemporalField = WeekFields.of(DayOfWeek.MONDAY, 1).weekOfWeekBasedYear()
    date.get(weekOfYear)

  }

}

/**
  * Implementation of [[Flight]] used to store data parsed from file.
  */
case class Flight(year: Int, quarter: Int, month: Int, dayOfMonth: String, dayOfWeek: String, weekOfYear: Int,
                  flightDate: String, origin: String, dest: String)
