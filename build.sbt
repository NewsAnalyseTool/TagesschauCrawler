ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "TageschauCrawler"
  )

libraryDependencies ++= Seq(
  "com.lihaoyi" %% "requests" % "0.8.0",
  "com.typesafe.play" %% "play-json" % "2.9.4",
  "com.lihaoyi" %% "requests" % "0.8.0",
  "org.jsoup" % "jsoup" % "1.14.3",
  "org.mongodb.scala" %% "mongo-scala-driver" % "4.3.0",
  "org.reactivemongo" %% "reactivemongo" % "1.0.10",
  "com.typesafe" % "config" % "1.4.3",
  "org.slf4j" % "slf4j-api" % "2.0.5",
  "org.slf4j" % "slf4j-simple" % "2.0.5"
)
