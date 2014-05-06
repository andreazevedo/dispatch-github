libraryDependencies += "org.scala-lang.modules" %% "scala-async" % "0.9.1"

libraryDependencies += "net.databinder.dispatch" %% "dispatch-core" % "0.11.1"

libraryDependencies += "net.databinder.dispatch" %% "dispatch-lift-json" % "0.11.0"

libraryDependencies += "org.specs2" %% "specs2" % "1.12.3" % "test"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2"

parallelExecution in Test := false

name := "dispatch-github"

organization := "dispatch"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.4"