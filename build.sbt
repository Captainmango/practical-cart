import org.typelevel.sbt.tpolecat.*

ThisBuild / organization := "cloud.eheaver"
ThisBuild / scalaVersion := "3.9.0"

// This disables fatal-warnings for local development. To enable it in CI set the `SBT_TPOLECAT_CI` environment variable in your pipeline.
// See https://github.com/typelevel/sbt-tpolecat/?tab=readme-ov-file#modes
ThisBuild / tpolecatDefaultOptionsMode := VerboseMode

// Required for Scalafix semantic rules (e.g. RemoveUnused). On Scala 3 the
// SemanticDB generator is built into the compiler, so no version is needed.
ThisBuild / semanticdbEnabled := true

lazy val root = (project in file(".")).settings(
  name := "practical-cart",
  libraryDependencies ++= Seq(
    // "core" module - IO, IOApp, schedulers
    // This pulls in the kernel and std modules automatically.
    "org.typelevel" %% "cats-effect" % "3.7.1",
    // concurrency abstractions and primitives (Concurrent, Sync, Async etc.)
    "org.typelevel" %% "cats-effect-kernel" % "3.7.1",
    // standard "effect" library (Queues, Console, Random etc.)
    "org.typelevel" %% "cats-effect-std" % "3.7.1",
    "org.typelevel" %% "munit-cats-effect" % "2.2.1" % Test)
)
