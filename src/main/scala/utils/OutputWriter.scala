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
    * Writes map to file.
    * @param map: contains parsed flights data.
    * @param out: path to save data.
    */
  def writeToFile(map: Map[String, Int], out: String): Unit = {

    printf("Saving results to %s\n\n", out)

    val writer = new PrintWriter(new File(out))

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
    * Writes map to file. Adds weekly data to file.
    * @param map: contains parsed flights data.
    * @param week: [Int] containing the week number.
    * @param out: path to save data.
    * @param append: [Boolean] trigger that defines whether to re-write file or to add data to it.
    */
  // I'm sorry for such implementation
  def writeToFile(map: Map[String, Int], week: Int, out: String, append: Boolean = false): Unit = {

    if (week == 1) printf("Saving results to %s\n\n", out)

    val writer = new PrintWriter(new FileOutputStream(new File(out), append))
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

}