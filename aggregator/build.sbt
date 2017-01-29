scalaVersion := "2.12.1"

scalacOptions := Seq("-encoding", "utf8")

libraryDependencies ++= Seq(
  "com.github.nscala-time" %% "nscala-time" % "2.16.0",
  "io.spray" %% "spray-json" % "1.3.3",
  "commons-io" % "commons-io" % "2.5",
  "com.typesafe.akka" %% "akka-http" % "10.0.3",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.3",
  "com.github.tototoshi" %% "scala-csv" % "1.3.4"
)
