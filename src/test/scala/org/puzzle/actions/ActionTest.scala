package org.puzzle
package actions

import org.puzzle.common.MoveBlockException
import org.puzzle.field.{Field, GameField}

class ActionTest extends PuzzleTest {
  "Up" should "move up blank block" in {
    val before = Vector(
      14, 1, 6, 2,
      10, 12, 8, 5,
      7, 3, 9, 13,
      11, 0, 15, 4)

    val after = Vector(
      14, 1, 6, 2,
      10, 12, 8, 5,
      7, 0, 9, 13,
      11, 3, 15, 4
    )
    val upF = Up.doAction(Field(4, createBlocks(before)))

    checkField(upF, 4)

    upF.asInstanceOf[GameField].blocks should be(createBlocks(after))
  }

  it should "Failure for top position" in {
    val before = Vector(
      14, 0, 6, 2,
      10, 12, 8, 5,
      7, 3, 9, 13,
      11, 1, 15, 4)

    intercept[MoveBlockException] {
      Up.doAction(Field(4, createBlocks(before)))
    }
  }

  "Down" should "move down blank block" in {
    val before = Vector(
      14, 1, 6, 2,
      10, 0, 8, 5,
      7, 3, 9, 13,
      11, 12, 15, 4)

    val after = Vector(
      14, 1, 6, 2,
      10, 3, 8, 5,
      7, 0, 9, 13,
      11, 12, 15, 4
    )
    val upF = Down.doAction(Field(4, createBlocks(before)))

    checkField(upF, 4)

    upF.asInstanceOf[GameField].blocks should be(createBlocks(after))
  }

  it should "Failure for bottom position" in {
    val before = Vector(
      14, 1, 6, 2,
      10, 12, 8, 5,
      7, 3, 9, 13,
      11, 0, 15, 4)

    intercept[MoveBlockException] {
      Down.doAction(Field(4, createBlocks(before)))
    }
  }

  "Left" should "move left blank block" in {
    val before = Vector(
      14, 1, 6, 2,
      10, 0, 8, 5,
      7, 3, 9, 13,
      11, 12, 15, 4)

    val after = Vector(
      14, 1, 6, 2,
      0, 10, 8, 5,
      7, 3, 9, 13,
      11, 12, 15, 4)

    val upF = Left.doAction(Field(4, createBlocks(before)))

    checkField(upF, 4)

    upF.asInstanceOf[GameField].blocks should be(createBlocks(after))
  }

  it should "Failure for left position" in {
    val before = Vector(
      14, 1, 6, 2,
      10, 12, 8, 5,
      7, 3, 9, 13,
      0, 11, 15, 4)

    intercept[MoveBlockException] {
      Left.doAction(Field(4, createBlocks(before)))
    }
  }

  "Right" should "move right blank block" in {
    val before = Vector(
      14, 1, 6, 2,
      10, 0, 8, 5,
      7, 3, 9, 13,
      11, 12, 15, 4)

    val after = Vector(
      14, 1, 6, 2,
      10, 8, 0, 5,
      7, 3, 9, 13,
      11, 12, 15, 4)

    val upF = Right.doAction(Field(4, createBlocks(before)))

    checkField(upF, 4)

    upF.asInstanceOf[GameField].blocks should be(createBlocks(after))
  }

  it should "Failure for right position" in {
    val before = Vector(
      14, 1, 6, 2,
      10, 12, 8, 5,
      7, 3, 9, 13,
      4, 11, 15, 0)

    intercept[MoveBlockException] {
      Right.doAction(Field(4, createBlocks(before)))
    }
  }
}
