package ru.dzdev
package shoppinglist

import cats.implicits.{catsSyntaxOptionId, toFunctorOps}
import com.bot4s.telegram.api.RequestHandler
import com.bot4s.telegram.api.declarative.Commands
import com.bot4s.telegram.clients.FutureSttpClient
import com.bot4s.telegram.future.{Polling, TelegramBot}
import com.bot4s.telegram.methods.ParseMode
import sttp.client3.akkahttp.AkkaHttpBackend

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future

class ShoppingListBot(val token: String) extends TelegramBot with Polling with Commands[Future] {

  implicit val backend = AkkaHttpBackend()
  override val client: RequestHandler[Future] = new FutureSttpClient(token)

  val list = new ArrayBuffer[String]()

  onCommand("help" or "h" or "помощь" or "п") { implicit msg =>
    reply(
      """
        |/help (/h /помощь /п) - show this message
        |/add (/a /добавить /д) - put new item into shopping list (/add Творог)
        |/mark (/m /вычеркнуть /в) - mark item as resolved
        |/list (/l /список /с) - show a shopping list
        |/remove (/r /удалить /у) - remove item from shopping list by position (/remove 2)
        |/clear (/c /очистить /о) - clear shopping list
        |""".stripMargin
    ).void
  }

  onCommand("add" or "a" or "добавить" or "д") { implicit msg =>
    val item = msg.text
      .map { it => it.substring(it.indexOf(" ") + 1) }
      .map { it =>
        it
          .split(",")
          .map{s => list +=s.trim}
      }
    item match {
      case Some(x) => reply(
        s"Ok, new item: ${x.map{s => s"<b>$s</b>"}.mkString(",")}",
        parseMode = ParseMode.HTML.some,
        replyToMessageId = msg.messageId.some
      ).void
      case None => reply(
        "I can't add empty to shopping list",
        parseMode = ParseMode.HTML.some,
        replyToMessageId = msg.messageId.some).void
    }
  }

  onCommand("mark" or "m" or "вычеркнуть" or "в") { implicit msg =>
    val item = msg.text
      .map { it => it.substring(it.indexOf(" ") + 1) }
      .map { it =>
        val pos = it.toInt
        val prItem = list(pos - 1)
        list(pos - 1) = s"<strike>${list(pos - 1)}</strike>"
        prItem
      }
    item match {
      case Some(x) => reply(
        s"Item <b>$x</b> is marked",
        replyToMessageId = msg.messageId.some,
        parseMode = ParseMode.HTML.some).void
      case None => reply(
        "I can`t mark empty",
        parseMode = ParseMode.HTML.some,
        replyToMessageId = msg.messageId.some).void
    }
  }

  onCommand("list" or "l" or "список" or "с") { implicit msg =>
    val table = list.zip(1 until list.size + 1)
      .map { it =>
        s"${it._2}: ${it._1}"
      }.reduce { (a, b) => a + "\n" + b }
    table.length match {
      case 0 => reply(
        "Shopping list is empty",
        parseMode = ParseMode.HTML.some,
        replyToMessageId = msg.messageId.some).void
      case _ => reply(
        table,
        parseMode = ParseMode.HTML.some,
        replyToMessageId = msg.messageId.some
      ).void
    }
  }

  onCommand("remove" or "r" or "удалить" or "у") { implicit msg =>
    val product = msg.text
      .map { it => it.substring(it.indexOf(" ") + 1) }
      .map { it =>
        val deletedProduct = if (it.forall(Character.isDigit)) {
          val product = list(it.toInt - 1)
          list.remove(it.toInt - 1)
          product
        } else {
          val product = it.toLowerCase
          list.foreach { pr => }
          product
        }
        deletedProduct
      }
    product match {
      case Some(x) => reply(
        s"Item <b>$x</b> is deleted",
        parseMode = ParseMode.HTML.some,
        replyToMessageId = msg.messageId.some).void
      case None => reply(
        "I can't delete",
        parseMode = ParseMode.HTML.some,
        replyToMessageId = msg.messageId.some).void
    }
  }

  onCommand("clear" or "c" or "очистить" or "о") { implicit msg =>
    list.clear()
    reply(
      "Shopping list is empty",
      parseMode = ParseMode.HTML.some,
      replyToMessageId = msg.messageId.some).void
  }
}
