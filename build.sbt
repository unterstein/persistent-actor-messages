organization := "info.unterstein"

name := "persistent-actors"

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.11",
// elasticsearch
  "org.elasticsearch" % "elasticsearch" % "2.2.0",
  "com.sksamuel.elastic4s" %% "elastic4s-streams" % "2.2.0",
// test
  "com.typesafe.akka" %% "akka-testkit" % "2.3.11" % "test",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)
