package com.example.api.v1.domain.repository

import com.example.api.v1.domain.`object`.todo.{NewTodo, Todo, TodoId, Todos}

trait TodoRepository[F[_]] {
  def save(todo: Todo): F[Unit]
  def save(todo: NewTodo): F[Unit]
  def findBy(id: TodoId): F[Option[Todo]]
  def findAll: F[Todos]
  def deleteBy(id: TodoId): F[Unit]
}
