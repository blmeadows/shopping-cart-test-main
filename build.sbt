ThisBuild / organization := "com.example"
ThisBuild / scalaVersion := "2.13.5"

val catsEffectVersion = "3.3.12"
val http4sVersion = "0.23.18"
val circeVersion = "0.14.4"

lazy val root = (project in file(".")).settings(
  name := "cats-effect-3-quick-start",
  libraryDependencies ++= Seq(
    // "core" module - IO, IOApp, schedulers
    // This pulls in the kernel and std modules automatically.
    "org.typelevel" %% "cats-effect" % catsEffectVersion,
    // concurrency abstractions and primitives (Concurrent, Sync, Async etc.)
    "org.typelevel" %% "cats-effect-kernel" % catsEffectVersion,
    // standard "effect" library (Queues, Console, Random etc.)
    "org.typelevel" %% "cats-effect-std" % catsEffectVersion,
    // better monadic for compiler plugin as suggested by documentation
    compilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
    "org.typelevel" %% "munit-cats-effect-3" % "1.0.7" % Test,
    "com.alejandrohdezma" %% "http4s-munit" % "0.15.0" % Test,
    // Typeful, functional, streaming HTTP with json support
    "org.http4s" %% "http4s-ember-client" % http4sVersion,
    "org.http4s" %% "http4s-circe"        % http4sVersion,
    "io.circe"   %% "circe-generic" % circeVersion
  )
)
