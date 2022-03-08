package com.example.api.v1.presentation.response

import com.example.api.v1.domain.`object`.todo.Todo

case class TodoResponseBody(
  id: Int,
  title: String,
  status: String
)

object TodoResponseBody {
  def of(todo: Todo): TodoResponseBody = TodoResponseBody(
    id = todo.id.toInt,
    title = todo.title.toString,
    status = todo.status.toString
  )
}
