package org.puzzle.actions

import org.puzzle.PuzzleTest

class FieldGeneratorTest extends PuzzleTest {
  "Random Generator" should "generate correct random field" in {
    checkField(FieldGenerator.randomField(10, 4), 4)
    checkField(FieldGenerator.randomField(1000, 4), 4)
    checkField(FieldGenerator.randomField(10000, 4), 4)
    checkField(FieldGenerator.randomField(100000, 4), 4)
  }
}
