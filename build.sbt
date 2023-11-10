ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "TageschauCrawler"
  )

libraryDependencies += "com.lihaoyi" %% "requests" % "0.8.0"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.9.4"

libraryDependencies += "com.lihaoyi" %% "requests" % "0.8.0"

libraryDependencies += "org.jsoup" % "jsoup" % "1.14.3"

libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "4.3.0"

libraryDependencies += "org.reactivemongo" %% "reactivemongo" % "1.0.10"
