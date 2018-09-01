package org.puzzle
package field

class FieldTest extends PuzzleTest {
  "Field" should "create GameField object for \"unordered\" blocks" in {
    val f = Field(4, createBlocks(correctBlocks).patch(5, Seq(BlankBlock), 0))
    checkField(f, 4)
    f shouldBe a[GameField]
  }

  it should "create FinishField object for \"ordered\" blocks" in {
    val f = Field(4, createBlocks(correctBlocks.sorted) :+ BlankBlock)
    checkField(f, 4)
    f shouldBe a[FinishedField]
  }

  it should "check if blocks are unique" in {
    intercept[IllegalArgumentException] {
      Field(4, createBlocks(correctBlocks.updated(0, 1)) :+ BlankBlock)
    }
  }

  it should "check if blocks contains BlankBlock" in {
    intercept[IllegalArgumentException] {
      Field(4, createBlocks(correctBlocks :+ 16))
    }
  }

  "Random Generator" should "generate correct random field" in {
    val field = Field(4)
    checkField(field, 4)
  }
}
