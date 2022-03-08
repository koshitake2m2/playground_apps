package com.example.api.v1.usecase

import com.example.api.v1.domain.`object`.todo.Todos
import com.example.api.v1.domain.repository.TodoRepository

class GetAllTodos[F[_]](
  repository: TodoRepository[F]
) {
  def apply: F[Todos] = repository.findAll
}
