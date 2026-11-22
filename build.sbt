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
    // retry actions that can fail
    "com.github.cb372" %% "cats-retry" % "4.0.0",
    // JSON parsing lib (used to make encoders)
    "io.circe" %% "circe-core" % "0.14.17",
    "io.circe" %% "circe-generic" % "0.14.17",
    "io.circe" %% "circe-parser" % "0.14.17",
    // Config library
    "is.cir" %% "ciris" % "3.15.2",
    // HTTP libs
    "org.http4s" %% "http4s-ember-client" % "0.23.39",
    "org.http4s" %% "http4s-ember-server" % "0.23.39",
    "org.http4s" %% "http4s-dsl" % "0.23.39",
    // Streaming in memory with no bloat
    "co.fs2" %% "fs2-core" % "3.16.0",
    // optional I/O library
    "co.fs2" %% "fs2-io" % "3.16.0",
    // optional reactive streams interop
    "co.fs2" %% "fs2-reactive-streams" % "3.16.0",
    // optional scodec interop
    "co.fs2" %% "fs2-scodec" % "3.16.0",
    // JWT auth for HTTP libs
    "dev.profunktor" %% "http4s-jwt-auth" % "2.0.15",
    // Immutable transformation stuff
    "dev.optics" %% "monocle-core" % "3.3.0",
    "dev.optics" %% "monocle-macro" % "3.3.0",
    // Logging
    // Only if you want to Support Any Backend
    "org.typelevel" %% "log4cats-core" % "2.8.0",
    // Direct Slf4j Support - Recommended
    "org.typelevel" %% "log4cats-slf4j" % "2.8.0",
    // Redis
    "dev.profunktor" %% "redis4cats-effects" % "2.0.6",
    "dev.profunktor" %% "redis4cats-streams" % "2.0.6",
    "dev.profunktor" %% "redis4cats-log4cats" % "2.0.6",
    // Refinement types for Scala 3
    "io.github.iltotore" %% "iron" % "3.4.1",
    // Strongly typed money
    "org.typelevel" %% "squants" % "1.8.3",
    // Postgres client
    "org.tpolecat" %% "skunk-core" % "1.0.0",
    // Property based tests
    "org.scalacheck" %% "scalacheck" % "1.20.0" % Test,
    "org.typelevel" %% "munit-cats-effect" % "2.2.1" % Test)
)
