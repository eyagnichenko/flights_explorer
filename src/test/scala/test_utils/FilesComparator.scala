package test_utils

import scala.io.Source._


/**
  * Implementation of FilesComparator responsible for comparing files by lines. Used for tests.
  */
class FilesComparator {

  /**
    * Checks, if files are equal by lines.
    * @param actualFilePath: [[String]] containing path to file with actual result.
    * @param expectedFilePath: [[String]] containing path to file with expected result.
    * @return [[Boolean]] value:
    *        - true: if files are equal
    *        - false: if files are not equal
    */
  def areEqual(actualFilePath: String, expectedFilePath: String): Boolean = {

    val actualFileLength = fromFile(actualFilePath).getLines.size
    val expectedFileLength = fromFile(expectedFilePath).getLines.size

    val actualFileLines = fromFile(actualFilePath).getLines
    val expectedFileLines = fromFile(expectedFilePath).getLines

    // count the number of lines in files which are not equal to each other
    val notEqualLinesCounter = (actualFileLines zip expectedFileLines).count {
      case (a, e) => a != e
    }

    // check if files are equal
    if (actualFileLength == expectedFileLength & notEqualLinesCounter == 0) true
    else false

  }

}