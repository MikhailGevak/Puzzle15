package org.puzzle
package actions

import org.puzzle.common.MoveBlockException
import org.puzzle.field.{EmptyField, Field, FinishedField, GameField}

object FieldAction {

  implicit class GameFieldExt(field: GameField) {
    def switchBlank(i: Int) = {
      val blocks = field.blocks
      val block = blocks(i)
      val blank = blocks(field.blankIndex)

      field.blocks.updated(i, blank).updated(field.blankIndex, block)
    }
  }

}

import org.puzzle.actions.FieldAction._

sealed trait FieldAction {
  val doAction: PartialFunction[Field, Field] = {
    case field@GameField(dimension, _, blankIndex) =>
      checkNewBlankPosition(dimension, blankIndex) match {
        case scala.util.Right(i)    => Field(dimension, field.switchBlank(i))
        case scala.util.Left(error) => throw MoveBlockException(error, field)
      }

    case field: FinishedField =>
      throw MoveBlockException("Game is finished!", field)
    case field: EmptyField    =>
      throw MoveBlockException("Game is not started!", field)
  }

  /**
    * Right: Calculates new position for blank block.
    * Left: Cause why new position can't be calculated.
    */
  def checkNewBlankPosition(dimension: Int, blankIndex: Int): Either[String, Int]
}

case object Up extends FieldAction {
  override def checkNewBlankPosition(dimension: Int, blankIndex: Int): Either[String, Int] = {
    if (blankIndex >= dimension) {
      scala.util.Right(blankIndex - dimension)
    }
    else {
      scala.util.Left("Can't move up!")
    }
  }
}

case object Down extends FieldAction {
  override def checkNewBlankPosition(dimension: Int, blankIndex: Int): Either[String, Int] = {
    if (blankIndex < dimension * dimension - dimension) {
      scala.util.Right(blankIndex + dimension)
    }
    else {
      scala.util.Left("Can't move down!")
    }
  }
}

case object Left extends FieldAction {
  override def checkNewBlankPosition(dimension: Int, blankIndex: Int): Either[String, Int] = {
    if (blankIndex % dimension != 0) {
      scala.util.Right(blankIndex - 1)
    }
    else {
      scala.util.Left("Can't move left!")
    }
  }
}

case object Right extends FieldAction {
  override def checkNewBlankPosition(dimension: Int, blankIndex: Int): Either[String, Int] = {
    if (blankIndex % dimension != dimension - 1) {
      scala.util.Right(blankIndex + 1)
    }
    else {
      scala.util.Left("Can't move right!")
    }
  }
}