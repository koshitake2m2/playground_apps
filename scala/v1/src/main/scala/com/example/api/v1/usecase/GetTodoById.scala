package com.example.api.v1.usecase

import com.example.api.v1.domain.`object`.todo.{Todo, TodoId}
import com.example.api.v1.domain.repository.TodoRepository

class GetTodoById[F[_]](
  repository: TodoRepository[F]
) {
  def apply(id: TodoId): F[Option[Todo]] = repository.findBy(id)
}
