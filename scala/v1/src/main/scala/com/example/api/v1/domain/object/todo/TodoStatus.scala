package com.example.api.v1.domain.`object`.todo

import com.example.api.v1.lib.AssertionErrors

sealed abstract class TodoStatus(override val toString: String)

object TodoStatus {
  case object Waiting extends TodoStatus("waiting")
  case object Doing extends TodoStatus("doing")
  case object Done extends TodoStatus("done")

  private val values: Map[String, TodoStatus] =
    Seq(Waiting, Doing, Done).map(status => status.toString -> status).toMap

  def apply(statusStr: String): TodoStatus =
    values.getOrElse(
      statusStr,
      throw AssertionErrors.one(NotMatchError(statusStr))
    )

  case class NotMatchError(input: String) extends AssertionError

}
