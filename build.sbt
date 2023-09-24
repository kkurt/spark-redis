ThisBuild / organization := "com.redislabs"
ThisBuild / version := "3.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "spark-redis",
    maintainer := "kursat@xworksglobal.com"
  )

Compile / scalacOptions ++= Seq(
  "-target:11",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xlog-reflective-calls",
  "-Xlint")
Compile / doc := file("")
Compile / run / fork := true
javaOptions ++= Seq("-Xdebug", "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005")
Compile / javacOptions ++= Seq("-Xlint:unchecked", "-Xlint:deprecation")
Compile / javaOptions ++= Seq("-Dconfig.resource=dev1.conf",
  "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED"
)

Test / parallelExecution := false
Test / testOptions += Tests.Argument("-oDF")
Test / logBuffered := false

//run / fork := false
Global / cancelable := false // ctrl-c

lazy val props = new {
  val projectBuildSourceEncoding = "UTF-8"
  val projectReportingOutputEncoding = "UTF-8"
  val javaVersion = "1.8"
  val scalaMajorVersion = "2.13"
  val scalaCompleteVersion = "2.13.12"
  val jedisVersion = "5.0.0"
  val pluginsScalatestVersion = "1.0"

}
val SparkVersion = "3.5.0"
libraryDependencies ++= Seq(
  "redis.clients" % "jedis" % props.jedisVersion,
  "org.apache.spark" %% "spark-core" % SparkVersion,
  "org.apache.spark" %% "spark-streaming" % SparkVersion,
  "org.apache.spark" %% "spark-sql" % SparkVersion,
  "org.scalatest" % s"scalatest_${props.scalaMajorVersion}" % "3.0.8" % Test
)
