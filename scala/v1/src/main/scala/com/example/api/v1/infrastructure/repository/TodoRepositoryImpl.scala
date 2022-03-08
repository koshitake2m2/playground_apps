package com.example.api.v1.infrastructure.repository

import cats.data.OptionT
import cats.effect.kernel.MonadCancelThrow
import cats.implicits._
import com.example.api.v1.domain.`object`.todo.{NewTodo, Todo, TodoId, Todos}
import com.example.api.v1.domain.repository.TodoRepository
import com.example.api.v1.infrastructure.dao.DoobieTodoDao
import com.example.api.v1.infrastructure.dto.{NewTodoDto, TodoDto}

import scala.language.higherKinds

class TodoRepositoryImpl[F[_]: MonadCancelThrow](
  dao: DoobieTodoDao[F]
) extends TodoRepository[F] {
  override def save(todo: Todo): F[Unit] = dao.update(TodoDto.of(todo))

  override def save(todo: NewTodo): F[Unit] =
    dao.saveAndReturnKey(NewTodoDto.of(todo)).void

  override def findBy(id: TodoId): F[Option[Todo]] =
    OptionT(dao.findBy(id)).map(_.toDomain).value

  override def findAll: F[Todos] = dao.findAll().map(dtos => Todos(dtos.map(_.toDomain).toList))

  override def deleteBy(id: TodoId): F[Unit] = dao.deleteBy(id)
}
