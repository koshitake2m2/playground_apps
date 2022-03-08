package com.example.api.v1.presentation.request

import cats.implicits._
import com.example.api.v1.domain.`object`.todo.{NewTodo, TodoStatus, TodoTitle}
import com.example.api.v1.lib.AssertionErrors
import com.example.api.v1.presentation.response.CreateTodoValidationErrorResponseBody

import scala.util.chaining.scalaUtilChainingOps

case class CreateTodoRequestBody(
  title: String,
  status: String
) {

  /** リクエストの検証. */
  def validate: Either[CreateTodoValidationErrorResponseBody, NewTodo] = {
    val titleOrErrors = validateTitle(title)
    val statusOrErrors = validateStatus(status)
    (titleOrErrors, statusOrErrors).mapN(NewTodo.apply).leftMap { _ =>
      CreateTodoValidationErrorResponseBody(
        titleOrErrors.swap.toOption.map(_.underlying.map(convertErrorMessage)),
        statusOrErrors.swap.toOption.map(_.underlying.map(convertErrorMessage))
      )
    }
  }

  private def validateTitle(title: String): Either[AssertionErrors, TodoTitle] =
    Either
      .catchOnly[AssertionErrors](TodoTitle(title))

  private def validateStatus(status: String): Either[AssertionErrors, TodoStatus] =
    Either
      .catchOnly[AssertionErrors](TodoStatus(status))

  /** 表明エラーをエラーメッセージに変換する. */
  private def convertErrorMessage(assertionError: AssertionError): String =
    (assertionError match {
      case TodoTitle.EmptyError => ValidationErrorMessage.TodoTitle.Empty
      case TodoTitle.TooLongError(input) => ValidationErrorMessage.TodoTitle.TooLong(input)
      case TodoStatus.NotMatchError(input) => ValidationErrorMessage.TodoStatus.NotMatch(input)
      case _ => assertionError
    }).pipe(_.toString)
}
