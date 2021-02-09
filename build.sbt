import Dependencies._

ThisBuild / scalaVersion := "2.13.4"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.example"
ThisBuild / organizationName := "example"

val http4sVersion = "1.0.0-M16"

lazy val root = (project in file("."))
  .settings(
    name := "ce101",
    libraryDependencies ++= Seq(
      "org.jsoup" % "jsoup" % "1.13.1",
      "org.typelevel" %% "cats-core" % "2.3.1",
      "org.typelevel" %% "cats-effect" % "3.0.0-M5",
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.http4s" %% "http4s-blaze-client" % http4sVersion,
      "org.slf4j" % "slf4j-simple" % "1.7.12",
      scalaTest % Test
    )
  )

addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.0")

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
