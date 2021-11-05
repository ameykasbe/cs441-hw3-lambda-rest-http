name := "REST"

version := "0.1"

scalaVersion := "2.12.4"
val akkaVersion = "2.5.26"
val akkaHttpVersion = "10.1.11"

libraryDependencies ++= Seq(
  // Akka Streams
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,

  // Akka HTTP
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,

)


val typesafeConfigVersion = "1.4.1"

val scalacticVersion = "3.2.9"

val logbackVersion = "1.3.0-alpha10"
val sfl4sVersion = "2.0.0-alpha5"

// Typesafe config dependencies
libraryDependencies += "com.typesafe" % "config" % typesafeConfigVersion

// Scalatest dependencies
libraryDependencies += "org.scalactic" %% "scalactic" % scalacticVersion
libraryDependencies += "org.scalatest" %% "scalatest" % scalacticVersion % Test
libraryDependencies += "org.scalatest" %% "scalatest-featurespec" % scalacticVersion % Test

// Logback dependencies
libraryDependencies += "ch.qos.logback" % "logback-core" % logbackVersion
libraryDependencies += "ch.qos.logback" % "logback-classic" % logbackVersion
libraryDependencies += "org.slf4j" % "slf4j-api" % sfl4sVersion