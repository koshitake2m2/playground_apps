package com.example.api.v1.domain.`object`.todo

import cats.data._
import cats.implicits._
import com.example.api.v1.lib.AssertionErrors

case class TodoTitle(override val toString: String) {
  import TodoTitle._
  assertion()

  /** 表明.
    * @throws AssertionErrors
    */
  private def assertion(): Unit =
    (assertEmpty() |+| assertLength()).leftMap(e => throw AssertionErrors(e))
  private def assertEmpty(): ValidatedNec[AssertionError, Unit] = Validated.condNec(toString.nonEmpty, (), EmptyError)
  private def assertLength(): ValidatedNec[AssertionError, Unit] =
    Validated.condNec(toString.length <= maxLength, (), TooLongError(toString))
}

object TodoTitle {
  val maxLength: Int = 250
  case object EmptyError extends AssertionError
  case class TooLongError(input: String) extends AssertionError
}
