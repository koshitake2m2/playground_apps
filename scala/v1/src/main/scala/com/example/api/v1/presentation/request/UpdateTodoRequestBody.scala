package com.example.api.v1.presentation.request

import cats.data._
import cats.implicits._
import com.example.api.v1.domain.`object`.todo.{Todo, TodoId, TodoStatus, TodoTitle}

case class UpdateTodoRequestBody(
  title: String,
  status: String
) {

  /** リクエストの検証. */
  // TODO: CreateTodoRequestBodyの検証と同じようにする.
  def validateWith(todoIdInt: Int): ValidatedNec[ValidationErrorMessage, Todo] =
    (validateTitle, validateStatus).mapN(Todo.apply(TodoId(todoIdInt), _, _))

  private def validateTitle: ValidatedNec[ValidationErrorMessage, TodoTitle] =
    Validated
      .catchNonFatal(TodoTitle(title))
      .leftMap(_ => ValidationErrorMessage.TodoTitle.TooLong(title))
      .toValidatedNec

  private def validateStatus: ValidatedNec[ValidationErrorMessage, TodoStatus] =
    Validated
      .catchNonFatal(TodoStatus(status))
      .leftMap(_ => ValidationErrorMessage.TodoStatus.NotMatch(title))
      .toValidatedNec
}
