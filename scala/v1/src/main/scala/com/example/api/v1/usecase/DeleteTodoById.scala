package com.example.api.v1.usecase

import com.example.api.v1.domain.`object`.todo.TodoId
import com.example.api.v1.domain.repository.TodoRepository

class DeleteTodoById[F[_]](
  repository: TodoRepository[F]
) {
  def apply(id: TodoId): F[Unit] = repository.deleteBy(id)
}
