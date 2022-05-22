ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "ShoppingList",
    idePackagePrefix := Some("ru.dzdev"),
    assembly / mainClass := Some("ru.dzdev.shoppinglist.EntryPoint"),
    assembly / assemblyJarName := "ShoppingListBot.jar"
  )

// Core with minimal dependencies, enough to spawn your first bot.
libraryDependencies += "com.bot4s" %% "telegram-core" % "5.4.2"

// Extra goodies: Webhooks, support for games, bindings for actors.
libraryDependencies += "com.bot4s" %% "telegram-akka" % "5.4.2"
libraryDependencies += "com.github.valskalla" %% "odin-core" % "0.13.0"
libraryDependencies += "com.softwaremill.sttp.client3" %% "core" % "3.6.2"
libraryDependencies += "com.softwaremill.sttp.client3" %% "akka-http-backend" % "3.6.2"