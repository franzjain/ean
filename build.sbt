name := """ean-module"""

version := "1.0-SNAPSHOT"

play.Project.playScalaSettings

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache
)
