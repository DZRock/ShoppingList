package ru.dzdev
package shoppinglist

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object EntryPoint {
  def main(args: Array[String]): Unit = {
    val bot = new ShoppingListBot(args(0))
    val eol = bot.run()
    scala.io.StdIn.readLine()
    bot.shutdown()
    Await.result(eol, Duration.Inf)
  }
}
