import org.scalajs.linker.interface.ModuleSplitStyle

import scala.sys.process.Process

import org.typelevel.scalacoptions.ScalacOptions

ThisBuild / scalaVersion     := "2.13.10"
ThisBuild / organization     := "de.flwi"
ThisBuild / organizationName := "Florian Witteler"
ThisBuild / startYear        := Some(2023)
ThisBuild / licenses         := Seq(License.Apache2)


lazy val buildFrontend = taskKey[Seq[(File, String)]](
  "Build the frontend, for production, and return all files generated."
)
lazy val baseUri = settingKey[String](
  """Base URI of the backend, defaults to `""` (empty string)."""
)

lazy val frontend = project
  .in(file("./"))
  .enablePlugins(ScalaJSPlugin)
  .enablePlugins(ScalablyTypedConverterExternalNpmPlugin)
  .enablePlugins(BuildInfoPlugin)
  .settings(
    name                            := "frontend",
    scalaVersion                    := "2.13.10", // important: adjust path in main.js import to point to the correct scalajs version in the target folder
    scalaJSUseMainModuleInitializer := true,
    tpolecatExcludeOptions ++= Set(
      ScalacOptions.warnValueDiscard,
      ScalacOptions.warnUnusedImports,
      ScalacOptions.warnUnusedLocals,
    ),
    scalacOptions ++= Seq(
      "-no-indent"
    ),
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
        .withModuleSplitStyle(ModuleSplitStyle.SmallModulesFor(List("de.flwi.sbtpluginshenanigans")))
    },
    externalNpm := {
      Process("yarn", baseDirectory.value).!
      baseDirectory.value
    },
    libraryDependencies ++= Seq(
      "com.raquo"                    %%% "laminar"         % "17.0.0-M2",
      "com.raquo"                    %%% "waypoint"        % "8.0.0-M1",
      "com.lihaoyi"                  %%% "upickle"         % "3.1.3",
      "com.disneystreaming.smithy4s" %%% "smithy4s-core"   % "0.18.3",
      "com.disneystreaming.smithy4s" %%% "smithy4s-http4s" % "0.18.3",
      "io.circe"                     %%% "circe-generic"   % "0.15.0-M1",
      "io.circe"                     %%% "circe-parser"    % "0.15.0-M1",
      "org.http4s"                   %%% "http4s-dom"      % "0.2.9",
      "org.http4s"                   %%% "http4s-client"   % "0.23.23",
    ),
    useYarn := true,
    baseUri := {
      if (insideCI.value) ""
      else
        // Vite will proxy this to the backend. See vite.config.js
        "/api"
    },
    buildInfoKeys    := Seq[BuildInfoKey](baseUri),
    buildInfoPackage := "smithy4s_codegen",
    buildFrontend := {
      import sys.process._

      val yarnInstall = Seq("yarn", "install")
      val yarnBuild   = Seq("yarn", "build")

      val dir    = baseDirectory.value
      val logger = sLog.value

      def runIn(dir: File)(cmd: Seq[String]): Int = {
        Process(cmd, cwd = Some(dir)).!(logger)
      }

      require(runIn(dir)(yarnInstall) == 0, s"[${yarnInstall.mkString(" ")}] failed.")
      require(runIn(dir)(yarnBuild) == 0, s"[${yarnInstall.mkString(" ")}] failed.")

      val distDir = dir / "dist"
      for {
        f        <- (distDir ** "*").get
        relative <- f.relativeTo(dir)
      } yield f -> s"$relative"
    },
  )
