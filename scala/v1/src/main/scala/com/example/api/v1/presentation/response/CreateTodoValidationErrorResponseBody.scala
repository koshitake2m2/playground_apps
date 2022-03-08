package com.example.api.v1.presentation.response

import cats.data.NonEmptyChain

case class CreateTodoValidationErrorResponseBody(
  title: Option[NonEmptyChain[String]],
  status: Option[NonEmptyChain[String]]
)
