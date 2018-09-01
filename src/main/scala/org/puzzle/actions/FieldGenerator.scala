package org.puzzle.actions

import org.puzzle.common.MoveBlockException
import org.puzzle.field.{EmptyField, Field, FinishedField, GameField}

import scala.annotation.tailrec
import scala.util.Try

object FieldGenerator {
  private[this] val random = scala.util.Random

  /**
    * Moves:
    * 0 - Up
    * 1 - Right
    * 2 - Down
    * 3 - Left
    */
  private[this] val suitableSteps = Map[FieldAction, Vector[FieldAction]](
    Up -> Vector(Up, Right, Left),
    Right -> Vector(Up, Right, Down),
    Down -> Vector(Right, Down, Left),
    Left -> Vector(Up, Down, Left))

  /** Generates random iterator for step. Next step can't be opposite to previous **/
  private[this] val randomMoves: Iterator[FieldAction] = new Iterator[FieldAction] {
    var lastStep: FieldAction = Up //First move can't be opposite do Up

    override def hasNext: Boolean = true

    override def next(): FieldAction = {
      val nextStep = suitableSteps get lastStep map { steps => steps(random.nextInt(3)) } getOrElse Up
      lastStep = nextStep
      nextStep
    }
  }

  @tailrec
  private[this] def doRandomMoves(field: Field, moves: Int): Field = {
    if (moves > 0) {
      val move = randomMoves.next()

      val (_field, _moves) = Try {
        move.doAction(field)
      }.map {
        case field: FinishedField => throw MoveBlockException("Generator creates finished field!", field)
        case _: EmptyField        => sys.error("Empty Field in Random Generator!!!")
        case field: GameField     => field: Field
      }.map {
        (_, moves - 1)
      }.recover { case _: MoveBlockException =>
        (field, moves) //incorrect move, repeat random move
      }.get

      doRandomMoves(_field, _moves)
    }
    else {
      field
    }
  }

  def randomField(randomDeep: Int, dimension: Int): Field = doRandomMoves(Field.initialGameField(dimension), randomDeep)
}