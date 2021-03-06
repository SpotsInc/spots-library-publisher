import ohnosequences.sbt.GithubRelease.keys.ghreleaseNotes
import ohnosequences.sbt._
import sbt.Keys._
import sbtassembly.AssemblyPlugin.assemblySettings

lazy val commonSettings = Seq(
  organization := "com.spotsinc.publisher.gcc",
  version := "1.3.3",
  scalaVersion := "2.11.8",
  fork in run := true,
  parallelExecution in ThisBuild := false,
  parallelExecution in Test := false,
  ghreleaseNotes := {
    tagName => tagName.repr + " Added ability to post additional attributes with avro messages"
  },
  ghreleaseRepoOrg := "SpotsInc"
)

lazy val projectAssemblySettings = Seq(
  assemblyJarName in assembly := name.value + ".jar",
  assemblyMergeStrategy in assembly := {
    case "BUILD" => MergeStrategy.discard
    case PathList("org", "apache", "commons", "logging", xs@_*) => MergeStrategy.first
    case PathList("META-INF", "io.netty.versions.properties") => MergeStrategy.first
    case PathList("META-INF", "sun-jaxb.episode") => MergeStrategy.first
    case other => MergeStrategy.defaultMergeStrategy(other)
  }
)

lazy val commonResolvers = Seq(
  "Sonatype OSS" at "https://oss.sonatype.org/content/repositories/releases/",
  Resolver.sonatypeRepo("snapshots"),
  Resolver.typesafeRepo("releases"),
  Resolver.mavenLocal
)

lazy val versions = new {
  val gcs = "0.8.0-beta"
  val gcPubSub = "0.8.0"

  val akkaHttpCore = "10.0.0"
  val akkaHttpTestKit = "10.0.0"
  val akkaHttpSprayJson = "10.0.0"
  val akkaHttpJackson = "10.0.0"

  val cloudPubSub = "1.29"

  val slf4jScalaLogging = "2.1.2"
  val slf4jAPI = "1.7.22"
  val slf4jLog4j = "1.7.22"
  val logbackClassic = "1.1.9"
  val akkaHttpXml = "10.0.0"

  val jodaTime = "2.9.7"

  val avro4s = "1.8.0"
}

lazy val publisher = project.in(file("publisher")).
  settings(commonSettings: _*).
  settings(assemblySettings: _*).
  settings(projectAssemblySettings: _*).
  settings(
    name := "spots-library-publisher",
    resolvers ++= Seq(
      Resolver.bintrayRepo("websudos", "oss-releases"),
      "The New Motion Public Repo" at "http://nexus.thenewmotion.com/content/groups/public/"
    ) ++ commonResolvers,

    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http-core" % versions.akkaHttpCore,
      "com.typesafe.akka" %% "akka-http-testkit" % versions.akkaHttpTestKit,
      "com.typesafe.akka" %% "akka-http-spray-json" % versions.akkaHttpSprayJson,
      "com.typesafe.akka" %% "akka-http-jackson" % versions.akkaHttpJackson,
      "com.typesafe.akka" %% "akka-http-xml" % versions.akkaHttpXml,
      "com.spotify" % "async-google-pubsub-client" % versions.cloudPubSub,
      "org.slf4j" % "slf4j-api" % versions.slf4jAPI,
      "org.slf4j" % "log4j-over-slf4j" % versions.slf4jLog4j,
      "ch.qos.logback" % "logback-classic" % versions.logbackClassic,
      "joda-time" % "joda-time" % versions.jodaTime,
      "com.sksamuel.avro4s" %% "avro4s-core" % versions.avro4s
    )
  )