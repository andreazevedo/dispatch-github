resolvers += "Java.net Maven2 Repository" at "http://download.java.net/maven/2/"

resolvers += "apache" at "https://repository.apache.org/content/repositories/snapshots/"

resolvers += "typesafe" at "http://repo.typesafe.com/typesafe/snapshots/"

resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
                  "releases"  at "http://oss.sonatype.org/content/repositories/releases")
                    
libraryDependencies ++= Seq(
   "net.databinder.dispatch" %% "dispatch-core" % "0.9.5",
   "net.databinder.dispatch" %% "dispatch-lift-json" % "0.9.5",
   "org.specs2" %% "specs2" % "1.12.3" % "test"
//   "org.slf4j" % "slf4j-api" % "1.7.2",
//   "org.slf4j" % "slf4j-simple" % "1.7.2",
//   "ch.qos.logback" % "logback-core" % "1.0.6" 
)

parallelExecution in Test := false

name := "dispatch-github"

organization := "dispatch"

version := "0.1-SNAPSHOT"
