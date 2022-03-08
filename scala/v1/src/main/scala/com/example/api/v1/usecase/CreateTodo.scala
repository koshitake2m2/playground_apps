package com.example.api.v1.usecase

import com.example.api.v1.domain.`object`.todo.NewTodo
import com.example.api.v1.domain.repository.TodoRepository

class CreateTodo[F[_]](
  repository: TodoRepository[F]
) {
  def apply(todo: NewTodo): F[Unit] = repository.save(todo)
}
