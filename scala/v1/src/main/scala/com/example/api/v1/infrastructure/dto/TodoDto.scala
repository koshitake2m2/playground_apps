package com.example.api.v1.infrastructure.dto

import com.example.api.v1.domain.`object`.todo.{Todo, TodoId, TodoStatus, TodoTitle}

case class TodoDto(
  id: TodoId,
  title: TodoTitle,
  status: TodoStatus
) {
  def toDomain: Todo = Todo(id, title, status)
}

object TodoDto {
  def of(todo: Todo): TodoDto = TodoDto(
    todo.id,
    todo.title,
    todo.status
  )
}
