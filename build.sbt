libraryDependencies += "org.scala-lang.modules" %% "scala-async" % "0.9.1"

libraryDependencies += "net.databinder.dispatch" %% "dispatch-core" % "0.11.0"

libraryDependencies += "net.databinder.dispatch" %% "dispatch-lift-json" % "0.11.0"

libraryDependencies += "org.specs2" %% "specs2" % "1.12.3" % "test"

parallelExecution in Test := false

name := "dispatch-github"

organization := "dispatch"

version := "0.1-SNAPSHOT"
