import sbt.Keys.scalacOptions

ThisBuild / scalaVersion := "2.13.8"
ThisBuild / scalafmtOnCompile := true

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala backend",
    version := "0.1.0"
  )
  .aggregate(v1, v2)

lazy val catsVersion = "2.7.0"
lazy val catsEffectVersion = "3.3.8"
lazy val log4catsVersion = "2.2.0"
lazy val cats = Seq(
  "org.typelevel" %% "cats-core" % catsVersion,
  "org.typelevel" %% "cats-effect" % catsEffectVersion
) ++ log4cats

lazy val log4cats = Seq(
  "org.typelevel" %% "log4cats-core" % log4catsVersion,
  "org.typelevel" %% "log4cats-slf4j" % log4catsVersion,
  "org.slf4j" % "slf4j-api" % "1.7.36",
  "org.slf4j" % "slf4j-simple" % "1.7.36"
)

lazy val scalatest = Seq(
  "org.scalatest" %% "scalatest" % "3.2.11" % Test
)

lazy val mysql = Seq(
  "mysql" % "mysql-connector-java" % "8.0.28"
)

lazy val doobieVersion = "1.0.0-RC2"
lazy val doobie = Seq(
  "org.tpolecat" %% "doobie-core" % doobieVersion,
  "org.tpolecat" %% "doobie-h2" % doobieVersion,
  "org.tpolecat" %% "doobie-hikari" % doobieVersion,
  "org.tpolecat" %% "doobie-scalatest" % doobieVersion % Test
)

lazy val http4sVersion = "0.23.11"
lazy val http4s = Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion
)

lazy val circeVersion = "0.14.1"
lazy val circeConfigVersion = "0.8.0"
lazy val circe = Seq(
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-literal" % circeVersion,
  "io.circe" %% "circe-generic-extras" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "io.circe" %% "circe-config" % circeConfigVersion
)

// reference: https://docs.scala-lang.org/overviews/compiler-options/index.html
val commonScalacOptions = Seq(
  "-Xfatal-warnings",
  "-deprecation",
  "-unchecked",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:existentials",
  "-language:postfixOps"
)

lazy val v1 = project
  .in(file("v1"))
  .settings(
    assembly / mainClass := Some("com.example.api.v1.presentation.WebServer"),
    scalacOptions ++= commonScalacOptions,
    libraryDependencies := cats ++ scalatest ++ mysql ++ doobie ++ http4s ++ circe
  )

lazy val v2 = project
  .in(file("v2"))
  .settings(
    assembly / mainClass := Some("com.example.api.v2.Main"),
    scalacOptions ++= commonScalacOptions
  )
