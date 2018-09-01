package org.puzzle.flow
package impl

import java.io.{BufferedReader, PrintStream}

import akka.Done
import akka.stream.Supervision
import akka.stream.Supervision.Directive
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import org.puzzle.common.MoveBlockException
import org.puzzle.field._

import scala.concurrent.Future

case class UnknownCommandException(command: String) extends Exception

object ConsoleCommand {
  private[this] val newCommand = "(n|new) ([0-9]+)".r

  def unapply(command: String): Option[GameCommand] = command.toLowerCase match {
    case "up" | "u"                => Some(FieldUp)
    case "down" | "d"              => Some(FieldDown)
    case "left" | "l"              => Some(FieldLeft)
    case "right" | "r"             => Some(FieldRight)
    case "new" | "n"               => Some(NewField(1000))
    case newCommand(_, randomDeep) => Some(NewField(randomDeep.toInt))
    case "quit" | "q"              => Some(Quit)
    case _                         => None
  }
}

class ConsoleGraph(in: BufferedReader, out: PrintStream) extends InterfaceGraph {
  val decider: PartialFunction[Throwable, Directive] = {
    case UnknownCommandException(command)  =>
      out.println(s"Unknown command \'$command\'")
      Supervision.Resume
    case MoveBlockException(reason, field) =>
      out.println(reason)
      printField(field)
      Supervision.Resume
  }

  val source: Source[GameCommand, _] = Source.fromIterator { () => Iterator.continually(in.readLine) }
    .map {
      case ConsoleCommand(gameCommand) => gameCommand
      case command                     => throw UnknownCommandException(command)
    }

  val sink: Sink[Field, Future[Done]] = Flow.fromFunction[Field, Unit](printField)
    .toMat(Sink.ignore)(Keep.right)

  private[this] def printField(field: Field): Unit = field match {
    case GameField(dimension, blocks, _) => printBlocks(dimension, blocks)

    case FinishedField(dimension) =>
      out.println("Game is finished!")
      printBlocks(dimension, ((1 until dimension * dimension) :+ 0) map {
        Block(_)
      })

    case EmptyField(_) =>
      out.println("Field is not initialized!\nUse command \'new <random_moves>\' to create new field!")
  }

  private[this] def printBlocks(dimension: Int, blocks: Seq[Block]): Unit = blocks.grouped(dimension) foreach { blocks =>
    printBlocks(blocks)
    out.println()
  }

  private[this] def printBlocks(blocks: Seq[Block]): Unit = blocks foreach printBlock

  private[this] def printBlock(block: Block): Unit = block match {
    case NumberBlock(number) if number < 10 => out.print(s"0$number ")
    case NumberBlock(number)                => out.print(s"$number ")
    case BlankBlock                         => out.print(s"   ")
  }
}
