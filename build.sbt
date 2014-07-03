name := "scala boot"

scalaVersion := "2.10.3"

mainClass := Some("boot.Boot")

libraryDependencies ++= Seq(
    "org.scalaj" %% "scalaj-http" % "0.3.15"
)