import org.specs2._
import org.specs2.matcher.MatchResult

import test_utils.FilesComparator
import utils.OutputWriter


class OutputWriterSpec extends Specification {def is = s2"""
  Output data should be stored in files properly for:
    the whole period                                        $wholePeriod
    the separate week                                       $separateWeek
    the whole period weekly                                 $weekly

"""

  val outputWriter: OutputWriter = new OutputWriter
  val comparator: FilesComparator = new FilesComparator

  val actualResultsPath: String = "src/test/resources/actual/"
  val expectedResultsPath: String = "src/test/resources/expected/"

  def wholePeriod: MatchResult[Any] = {

    val fileName = "whole_period.txt"
    val arrivalsMap = Map("ABE" -> 137, "DAL" -> 3861, "MGM" -> 295, "PHX" -> 13335, "YUM" -> 264)
    outputWriter.writeToFile(arrivalsMap, actualResultsPath + fileName)
    comparator.checkIfFilesAreEqual(actualResultsPath + fileName, expectedResultsPath + fileName)

  } must_==true

  def separateWeek: MatchResult[Any] = {

    val fileName = "for_week.txt"
    val startsMap = Map("ABE" -> 10, "DAL" -> 562, "MGM" -> 41, "PHX" -> 2340, "YUM" -> 52)
    outputWriter.writeWeekDataToFile(1, startsMap, actualResultsPath + fileName)
    comparator.checkIfFilesAreEqual(actualResultsPath + fileName, expectedResultsPath + fileName)

  } must_==true
//
  def weekly: MatchResult[Any] = {

    val fileName = "weekly.txt"
    val weeklyMap = Map(1 -> Map("ABE" -> 10, "DAL" -> 562, "MGM" -> 41, "PHX" -> 2340, "YUM" -> 52),
      2 -> Map("ABE" -> 32, "DAL" -> 872, "MGM" -> 68, "PHX" -> 2966, "YUM" -> 58))
    outputWriter.writeToFileWeekly(weeklyMap, actualResultsPath + fileName)
    comparator.checkIfFilesAreEqual(actualResultsPath + fileName, expectedResultsPath + fileName)

  } must_==true

}