name := "scala bot"

scalaVersion := "2.10.3"

mainClass := Some("bot.bot")

libraryDependencies ++= Seq(
    "org.scalaj" %% "scalaj-http" % "0.3.15"
)