package org.puzzle
package field

sealed trait Block

case class NumberBlock(number: Int) extends Block

case object BlankBlock extends Block

object Block {
  def apply(number: Int): Block = {
    require(number >= 0)
    if (number > 0) {
      NumberBlock(number)
    }
    else {
      BlankBlock
    }
  }

  def unapply(block: Block): Option[Int] = block match {
    case NumberBlock(number) => Some(number)
    case BlankBlock          => Some(0)
  }
}

sealed trait Field {
  def dimension: Int
}

object Field {
  private[this] def initBlocks(dimension: Int) = ((1 until dimension * dimension) :+ 0).map {
    Block(_)
  }.toVector


  def apply(dimension: Int, blocks: Vector[Block]): Field = {
    checkBlocks(dimension, blocks)

    if (!isFinished(blocks)) {
      GameField(dimension, blocks, blocks.indexOf(BlankBlock))
    }
    else {
      FinishedField(dimension)
    }
  }

  def initialGameField(dimension: Int): Field = {
    val blocks = initBlocks(dimension)
    checkBlocks(dimension, blocks)
    GameField(dimension, blocks, blocks.indexOf(BlankBlock))
  }

  private[field] def isFinished(blocks: Vector[Block]) = blocks.sortWith {
    case (NumberBlock(block1), NumberBlock(block2)) => block1 <= block2
    case (BlankBlock, NumberBlock(_))               => false
    case _                                          => true
  } == blocks

  private[field] def checkBlocks(dimension: Int, blocks: Vector[Block]): Unit = {
    object numberKey
    object blankKey

    val blocksCount = dimension * dimension - 1

    val blocksMap = blocks.groupBy {
      case _: NumberBlock => numberKey
      case BlankBlock     => blankKey
    }

    require(blocksMap.get(blankKey).toVector.flatten.size == 1, "It has to be one Blank Block on a field!")
    val numberBlocks = blocksMap.get(numberKey).toVector.flatten.collect { case NumberBlock(number) => number }.sorted
    require(numberBlocks.size == blocksCount, s"It has to be ${blocksCount} number blocks, not ${numberBlocks.size}!")

    require(numberBlocks.toSet.size == numberBlocks.size, s"Incorrect blocks set: ${numberBlocks}!")

    ()
  }
}

case class GameField private[field](dimension: Int, blocks: Vector[Block], blankIndex: Int) extends Field

case class FinishedField private[field](dimension: Int) extends Field

case class EmptyField(dimension: Int) extends Field
