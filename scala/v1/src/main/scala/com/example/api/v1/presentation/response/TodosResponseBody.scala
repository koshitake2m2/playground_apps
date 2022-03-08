package com.example.api.v1.presentation.response

import com.example.api.v1.domain.`object`.todo.Todos
import com.example.api.v1.presentation.response.TodosResponseBody.ResponseBody

import scala.util.chaining.scalaUtilChainingOps

case class TodosResponseBody(
  todos: List[ResponseBody.Todo]
)

object TodosResponseBody {
  object ResponseBody {
    case class Todo(
      id: Int,
      title: String,
      status: String
    )
  }

  def of(todos: Todos): TodosResponseBody =
    todos.toList
      .map(todo =>
        ResponseBody.Todo(
          id = todo.id.toInt,
          title = todo.title.toString,
          status = todo.status.toString
        )
      )
      .pipe(TodosResponseBody.apply)
}
