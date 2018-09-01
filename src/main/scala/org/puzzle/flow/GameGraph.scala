package org.puzzle.flow

import akka.Done
import akka.stream.Supervision.Directive
import akka.stream.scaladsl.{Keep, RunnableGraph, Sink, Source}
import akka.stream.{ActorAttributes, Supervision}
import org.puzzle.actions.FieldGenerator
import org.puzzle.field.{EmptyField, Field}

import scala.concurrent.Future

sealed trait GameCommand

case object FieldUp extends GameCommand

case object FieldDown extends GameCommand

case object FieldLeft extends GameCommand

case object FieldRight extends GameCommand

case class NewField(randomDeep: Int) extends GameCommand

case object Quit extends Error with GameCommand

trait InterfaceGraph {
  val decider: PartialFunction[Throwable, Directive]
  val source: Source[GameCommand, _]
  val sink: Sink[Field, Future[Done]]
}

case class GameGraph(interface: InterfaceGraph) {
  val decider: PartialFunction[Throwable, Directive] = {
    case Quit         =>
      Supervision.Stop
    case error: Error =>
      error.printStackTrace()
      Supervision.Stop
  }

  def game(dimension: Int): RunnableGraph[Future[Done]] =
    interface.source.scan[Field](EmptyField(dimension)) {
      case (field, FieldUp)          => org.puzzle.actions.Up.doAction(field)
      case (field, FieldDown)        => org.puzzle.actions.Down.doAction(field)
      case (field, FieldLeft)        => org.puzzle.actions.Left.doAction(field)
      case (field, FieldRight)       => org.puzzle.actions.Right.doAction(field)
      case (_, NewField(randomDeep)) => FieldGenerator.randomField(randomDeep, dimension)
      case (_, Quit)                 => throw Quit
    }
      .withAttributes(ActorAttributes.supervisionStrategy(interface.decider orElse decider))
      .toMat(interface.sink) {
        Keep.right
      }
}
