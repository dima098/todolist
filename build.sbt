name := "ToDoList"

version := "1.0"

scalaVersion := "2.12.4"

resolvers ++= Seq(
  Resolver.bintrayRepo("hseeberger", "maven"),
  "Artima Maven Repository" at "http://repo.artima.com/releases"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.0.10",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.10" % Test,
  "com.typesafe.slick" %% "slick" % "3.2.1",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.1",
  "com.typesafe.play" %% "play-json" % "2.6.7",
  "de.heikoseeberger" %% "akka-http-play-json" % "1.12.0",
  "com.h2database" % "h2" % "1.0.60",
  "org.scalactic" %% "scalactic" % "3.0.4",
  "org.scalatest" %% "scalatest" % "3.0.4" % "test",
  "org.mockito" % "mockito-core" % "1.8.5" % Test
)
        