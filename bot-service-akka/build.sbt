name := "bot-service-akka"
organization := "com.bartender"
version := "1.0"
scalaVersion := "2.11.7"

libraryDependencies ++= {
  val akkaVersion = "2.4.11"
  val scalaTestV = "3.0.0"
  Seq(
    "com.typesafe.akka" %% "akka-http-core" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion % "test",
    "org.scalatest" %% "scalatest" % scalaTestV % "test",

    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "ch.qos.logback" % "logback-classic" % "1.1.7",
    "org.brunocvcunha.instagram4j" % "instagram4j" % "1.6",
    "commons-io" % "commons-io" % "2.6"
  )
}

compile in Compile := (compile in Compile dependsOn Def.task { (managedSourceDirectories in Compile).value.head.mkdirs() }).value

javacOptions in Compile ++= Seq("-s", (managedSourceDirectories in Compile).value.head.getAbsolutePath)