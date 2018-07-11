# flights_explorer
Flights Explorer is a tool that takes data about plane flights from archived CSV file, extracts and saves to text files statistics about:
- arrivals to each airport for the whole period
- starts from each airport for the whole period
- non-zero difference in total number of planes that arrived to and left from the airport
- arrivals to each airport for the whole period per each week

Functions are covered with tests.

Running
-------
* run the command via terminal to execute: `sbt run`
* run the tests with command: `sbt test`

Notes
-----
* tested on macOS Sierra, Ubuntu 16.04, Windows 7 and 10 with Scala 2.12.3
