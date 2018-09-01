package org.puzzle

import org.puzzle.field._
import org.scalatest.{FlatSpec, Matchers}

trait PuzzleTest extends FlatSpec with Matchers {
  val correctBlocks = Vector(14, 1, 6, 2, 10, 12, 8, 5, 7, 3, 9, 13, 11, 15, 4)

  def createBlocks(numbers: Vector[Int]) = {
    numbers collect {
      case i if i != 0 => NumberBlock(i)
      case _           => BlankBlock
    }
  }

  protected def checkField(field: Field, correctDim: Int) = field match {
    case GameField(dimension, blocks, blankIndex) =>
      dimension should be(correctDim)
      blankIndex should (be >= 0 and be < dimension * dimension)

      val intBlocks = blocks map {
        case NumberBlock(number) => number
        case BlankBlock          => 0
      }

      intBlocks.toSet.size should be(intBlocks.size) //All elements should be unique

      intBlocks.size should be(dimension * dimension)

      intBlocks.min should be(0)
      intBlocks.max should be(dimension * dimension - 1)

    case FinishedField(dimension) =>
      dimension should be(correctDim)
  }
}
