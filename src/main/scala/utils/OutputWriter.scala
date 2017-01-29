package utils

import java.io.{File, FileOutputStream, PrintWriter}


/**
  * Implementation of OutputWriter responsible for saving results to files.
  */
class OutputWriter() {

  // values indexes
  val airport = 0
  val counter = 1

  /**
    * Creates / re-writes file, containing data sorted by airports.
    * @param map: contains parsed flights data.
    * @param out: file path.
    */
  def writeToFile(map: Map[String, Int], out: String): Unit = {

    printf("Saving results to: %s.\n\n", out)

    val writer: PrintWriter = new PrintWriter(new File(out))

    val res = map.toSeq.sortBy(_._1)  // sort by airport name
    for (line <- res) {

      val toRemove = "()".toSet
      val str = line.toString().filterNot(toRemove)
      val values = str.split(",").map(_.trim)
      writer.write(values(airport) + " " + values(counter) + "\n")

    }
    writer.close()

  }

  /**
    * Creates / re-writes file, containing data about arrivals to airports per week sorted by airports.
    * @param week: [[Int]] containing number of week.
    * @param map: contains parsed flights data for week.
    * @param out: file path.
    * @param append: [[Boolean]] trigger:
    *                - false: re-write file
    *                - true: add data to file
    */
  def writeWeekDataToFile(week: Int, map: Map[String, Int], out: String, append: Boolean = false): Unit = {

    if (week == 1) printf("Saving results to %s.\n\n", out)

    val writer: PrintWriter = new PrintWriter(new FileOutputStream(new File(out), append))
    writer.write("W" + week + ":\n")

    val res = map.toSeq.sortBy(_._1)  // sort by airport name
    for (line <- res) {

      val toRemove = "()".toSet
      val str = line.toString().filterNot(toRemove)
      val values = str.split(",").map(_.trim)
      writer.write("\t" + values(airport) + " " + values(counter) + "\n")

    }
    writer.close()

  }

  /**
    * Creates / rewrites file, containing data about arrivals to airports sorted by weeks by invoking
    * writeWeekDataToFile method.
    * @param map contains parsed flights data.
    * @param out file path.
    */
  def writeToFileWeekly(map: Map[Int, Map[String, Int]], out: String): Unit = {

    var week: Int = 1
    var append: Boolean = false  // create / re-write file to save data for the 1st week

    while (week < map.size) {  // map.size is equal to amount of weeks in map

      val weekArrivals: Option[Map[String, Int]] = map.get(week)  // get arrivals for week
      writeWeekDataToFile(week, weekArrivals.get, out, append)
      append = true  // add data to file for next weeks
      week += 1

    }

  }

}