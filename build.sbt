name := "akka-playground"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.11",
  "com.typesafe.akka" %% "akka-persistence" % "2.4.11",
  "com.typesafe.akka" %% "akka-stream" % "2.4.11",
  "com.typesafe.akka" %% "akka-osgi" % "2.4.11",
  "com.typesafe.akka" %% "akka-cluster" % "2.4.11",

  "com.typesafe.akka" %% "akka-testkit" % "2.4.11",
  "org.scalactic" %% "scalactic" % "3.0.0",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test"
)

