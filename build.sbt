resolvers += "Java.net Maven2 Repository" at "http://download.java.net/maven/2/"

resolvers += "apache" at "https://repository.apache.org/content/repositories/snapshots/"

resolvers += "typesafe" at "http://repo.typesafe.com/typesafe/snapshots/"

libraryDependencies ++= Seq(
	"net.databinder" % "dispatch-core_2.9.1" % "0.8.7",
	"net.databinder" % "dispatch-http-json_2.9.1" % "0.8.7",
	"net.databinder" % "dispatch-oauth_2.9.1" % "0.8.7",
	"org.scala-tools.testing" % "specs_2.9.1" % "1.6.9" % "test",
	"net.liftweb" % "lift-json_2.9.1" % "2.5-M1"
)
