import scala.sys.process._
import scala.language.postfixOps

import sbtwelcome._

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val tyrianVersion = TyrianVersion.getVersion
lazy val scala3Version = "3.6.3"

lazy val commonSettings: Seq[sbt.Def.Setting[_]] = Seq(
  version      := tyrianVersion,
  scalaVersion := scala3Version,
  organization := "io.indigoengine",
  libraryDependencies ++= Seq(
    "org.scalameta" %%% "munit" % "1.1.0" % Test
  ),
  libraryDependencies ++= Seq(
    "io.indigoengine" %%% "tyrian-io" % tyrianVersion
  ),
  testFrameworks += new TestFramework("munit.Framework"),
  scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
  crossScalaVersions := Seq(scala3Version),
  scalafixOnCompile  := true,
  semanticdbEnabled  := true,
  semanticdbVersion  := scalafixSemanticdb.revision,
  autoAPIMappings    := true
)

lazy val bootstrap =
  (project in file("bootstrap"))
    .enablePlugins(ScalaJSPlugin)
    .settings(commonSettings: _*)
    .settings(name := "bootstrap")

lazy val bundler =
  (project in file("bundler"))
    .enablePlugins(ScalaJSPlugin)
    .enablePlugins(ScalaJSBundlerPlugin)
    .settings(commonSettings: _*)
    .settings(name := "bundler")
    .settings(
      // Source maps seem to be broken with bundler
      Compile / fastOptJS / scalaJSLinkerConfig ~= { _.withSourceMap(false) },
      Compile / fullOptJS / scalaJSLinkerConfig ~= { _.withSourceMap(false) },
      scalaJSUseMainModuleInitializer := true
    )

lazy val electron =
  (project in file("electron"))
    .enablePlugins(ScalaJSPlugin)
    .settings(commonSettings: _*)
    .settings(name := "electron")

lazy val field =
  (project in file("field"))
    .enablePlugins(ScalaJSPlugin)
    .settings(commonSettings: _*)
    .settings(name := "field")

lazy val http =
  (project in file("http"))
    .enablePlugins(ScalaJSPlugin)
    .settings(commonSettings: _*)
    .settings(
      name := "http",
      libraryDependencies ++= Seq(
        "io.circe" %%% "circe-core",
        "io.circe" %%% "circe-parser"
      ).map(_ % "0.14.10")
    )

lazy val http4sdom =
  (project in file("http4s-dom"))
    .enablePlugins(ScalaJSPlugin)
    .settings(commonSettings: _*)
    .settings(
      name := "http4s-dom",
      libraryDependencies ++= Seq(
        "io.circe" %%% "circe-core",
        "io.circe" %%% "circe-parser",
        "io.circe" %%% "circe-generic"
      ).map(_ % "0.14.10")
    )
    .settings(
      libraryDependencies ++= Seq(
        "org.http4s" %%% "http4s-dom"   % "0.2.11",
        "org.http4s" %%% "http4s-circe" % "0.23.30"
      )
    )

lazy val sttp =
  (project in file("sttp"))
    .enablePlugins(ScalaJSPlugin)
    .settings(commonSettings: _*)
    .settings(
      name := "sttp",
      libraryDependencies ++= Seq(
        "io.circe" %%% "circe-core",
        "io.circe" %%% "circe-generic"
      ).map(_ % "0.14.13")
    )
    .settings(
      libraryDependencies ++= Seq(
        "com.softwaremill.sttp.client4" %%% "core",
        "com.softwaremill.sttp.client4" %%% "cats",
        "com.softwaremill.sttp.client4" %%% "circe"
      ).map(_ % "4.0.8")
    )

lazy val mainlauncher =
  (project in file("main-launcher"))
    .enablePlugins(ScalaJSPlugin)
    .settings(commonSettings: _*)
    .settings(
      name                            := "main-launcher",
      Compile / mainClass             := Some("example.Main"),
      scalaJSUseMainModuleInitializer := true
    )

lazy val nonpm =
  (project in file("no-npm"))
    .enablePlugins(ScalaJSPlugin)
    .settings(commonSettings: _*)
    .settings(
      name := "No NPM",
      scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.NoModule) }
    )

lazy val tailwind =
  (project in file("tailwind"))
    .enablePlugins(ScalaJSPlugin)
    .settings(commonSettings: _*)
    .settings(name := "tailwind")

lazy val websocket =
  (project in file("websocket"))
    .enablePlugins(ScalaJSPlugin)
    .settings(commonSettings: _*)
    .settings(name := "websocket")

lazy val exampleProjects: List[String] =
  List(
    "bootstrap",
    "bundler",
    "electron",
    "http",
    "http4sdom",
    "sttp",
    "mainlauncher",
    "nonpm",
    "tailwind",
    "websocket"
  )

lazy val tyrianExamplesProject =
  (project in file("."))
    .enablePlugins(ScalaJSPlugin)
    .settings(commonSettings: _*)
    .settings(
      code := {
        val command = Seq("code", ".")
        val run = sys.props("os.name").toLowerCase match {
          case x if x contains "windows" => Seq("cmd", "/C") ++ command
          case _                         => command
        }
        run.!
      },
      name := "TyrianExamples"
    )
    .settings(
      logo := s"Tyrian Examples (v${version.value}):\n" + exampleProjects
        .map(s => " - " + s)
        .mkString("\n") + "\n",
      usefulTasks := Seq(
        UsefulTask("cleanAll", "Cleans all examples").noAlias,
        UsefulTask("compileAll", "Compiles all examples").noAlias,
        UsefulTask("testAll", "Tests all examples").noAlias,
        UsefulTask("fastLinkAll", "Compiles all examples to JS").noAlias,
        UsefulTask("code", "Launch VSCode").noAlias
      ),
      logoColor        := scala.Console.MAGENTA,
      aliasColor       := scala.Console.BLUE,
      commandColor     := scala.Console.CYAN,
      descriptionColor := scala.Console.WHITE
    )

lazy val code =
  taskKey[Unit]("Launch VSCode in the current directory")

def makeCmds(names: List[String]): Seq[UsefulTask] =
  names.zipWithIndex.map { case (n, i) =>
    val cmd =
      n match {
        case "bundler" =>
          List(
            s"$n/clean",
            s"$n/fastOptJS::webpack"
          ).mkString(";", ";", "")

        case _ =>
          List(
            s"$n/clean",
            s"$n/fastOptJS"
          ).mkString(";", ";", "")
      }

    UsefulTask(cmd, n).alias("build" + (i + 1))
  }.toSeq

// Top level commands
def applyCommand(projects: List[String], command: String): String =
  projects.map(p => p + "/" + command).mkString(";", ";", "")

def applyToAll(command: String): String =
  List(
    applyCommand(exampleProjects, command)
  ).mkString

addCommandAlias(
  "cleanAll",
  applyToAll("clean")
)
addCommandAlias(
  "compileAll",
  applyToAll("compile")
)
addCommandAlias(
  "testAll",
  applyToAll("test")
)
addCommandAlias(
  "fastLinkAll",
  applyToAll("fastLinkJS")
)
addCommandAlias(
  "buildExamples",
  List(
    "cleanAll",
    "compileAll",
    "fastLinkAll"
  ).mkString(";", ";", "")
)
