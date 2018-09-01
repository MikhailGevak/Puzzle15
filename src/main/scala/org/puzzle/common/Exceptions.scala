package org.puzzle.common

import org.puzzle.field.Field

case class MoveBlockException(reason: String, field: Field) extends Exception