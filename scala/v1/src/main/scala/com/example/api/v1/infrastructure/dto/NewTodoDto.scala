package com.example.api.v1.infrastructure.dto

import com.example.api.v1.domain.`object`.todo.{NewTodo, TodoStatus, TodoTitle}

case class NewTodoDto(
  title: TodoTitle,
  status: TodoStatus
)

object NewTodoDto {
  def of(todo: NewTodo): NewTodoDto = NewTodoDto(
    todo.title,
    todo.status
  )
}
