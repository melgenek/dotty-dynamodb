lazy val root = project
  .in(file("."))
  .settings(
    name := "dotty-dynamodb",
    version := "0.1",
    scalaVersion := "3.0.0-M3",
    useScala3doc := true,
    libraryDependencies ++= Seq(
      "software.amazon.awssdk" % "dynamodb" % "2.15.45",
      "org.slf4j" % "slf4j-simple" % "1.7.30"
    )
  )
