name := "tubelytics"

organization := "com.tube"

version := "1.0-SNAPSHOT"

// Enable Play framework with Java support
lazy val root = (project in file(".")).enablePlugins(PlayJava)

// Required Scala version for Play, even though the code is written in Java
scalaVersion := "2.13.15"

// Dependencies
libraryDependencies ++= Seq(
  guice, // Dependency injection for Play
  "com.typesafe.play" %% "play-ws" % "2.9.0",
  "com.google.api-client" % "google-api-client" % "1.33.2", // Google API client library
  "com.google.apis" % "google-api-services-youtube" % "v3-rev20241031-2.0.0", // YouTube API version
  "com.google.http-client" % "google-http-client" % "1.39.2", // Google HTTP client
  "com.google.code.gson" % "gson" % "2.10.1", // Gson for JSON processing
  "org.projectlombok" % "lombok" % "1.18.24", // Lombok for getter and setter generation
  "junit" % "junit" % "4.13.2" % Test,
  "org.mockito" % "mockito-core" % "3.6.0" % Test,
  "org.junit.jupiter" % "junit-jupiter-api" % "5.7.0" % Test,
  "org.mockito" % "mockito-junit-jupiter" % "3.6.0" % Test,
  "org.junit.platform" % "junit-platform-launcher" % "1.7.0" % Test,
  "org.junit.platform" % "junit-platform-runner" % "1.7.0" % Test,
  // Akka dependencies for actor-based programming
  "com.typesafe.akka" %% "akka-actor" % "2.6.20", // Core Akka actor library
  "com.typesafe.akka" %% "akka-stream" % "2.6.20", // Akka streams
  // Akka TestKit for testing purposes
  "com.typesafe.akka" %% "akka-testkit" % "2.6.21"  // Akka TestKit for testing
)

// Enable annotation processing
javacOptions ++= Seq(
  "-source", "9",
  "-target", "9",
  "-processorpath", "org.projectlombok:lombok:1.18.24"
)

// Add Google Maven repository for Google API dependencies
resolvers += "Google API Maven" at "https://maven.google.com/"
