name := "flights_explorer"
version := "1.0"
scalaVersion := "2.12.3"

libraryDependencies ++= Seq("org.specs2" %% "specs2-core" % "3.8.7" % "test")
scalacOptions in Test ++= Seq("-Yrangepos")

logLevel := sbt.Level.Info