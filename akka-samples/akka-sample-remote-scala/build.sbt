name := "akka-sample-remote-scala"

version := "2.4.16-aspect-SNAPSHOT"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.16-aspect",
  "com.typesafe.akka" %% "akka-remote" % "2.4.16-aspect"
)

licenses := Seq(("CC0", url("http://creativecommons.org/publicdomain/zero/1.0")))
