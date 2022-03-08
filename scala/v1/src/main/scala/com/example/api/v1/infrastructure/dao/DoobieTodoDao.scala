package com.example.api.v1.infrastructure.dao

import cats.effect.kernel.MonadCancelThrow
import cats.implicits._
import com.example.api.v1.domain.`object`.todo._
import com.example.api.v1.infrastructure.dto._
import doobie.implicits._
import doobie.util.transactor.Transactor
import doobie.util.{Get, Read}

class DoobieTodoDao[F[_]: MonadCancelThrow](transactor: Transactor[F]) {

  implicit private val todoStatusGet: Get[TodoStatus] =
    Get[String].map(TodoStatus.apply)
  implicit private val todoDtoRead: Read[TodoDto] =
    Read[(TodoId, TodoTitle, TodoStatus)].map { case (id, title, status) =>
      TodoDto(id, title, status)
    }

  def findAll(): F[Seq[TodoDto]] =
    sql"""
         select todo_id, todo_title, todo_status
         from sample_db.todo
         """.query[TodoDto].to[Seq].transact(transactor)

  def findBy(id: TodoId): F[Option[TodoDto]] =
    sql"""
         select todo_id, todo_title, todo_status
         from sample_db.todo
         where todo_id = $id
       """.query[TodoDto].option.transact(transactor)

  def saveAndReturnKey(dto: NewTodoDto): F[TodoId] =
    sql"""
         insert into sample_db.todo
         (todo_title, todo_status)
         values
         (${dto.title}, ${dto.status.toString})
       """.update
      .withUniqueGeneratedKeys[TodoId]("todo_id")
      .transact(transactor)

  def update(dto: TodoDto): F[Unit] =
    sql"""
         update sample_db.todo
         set todo_title = ${dto.title},
             todo_status = ${dto.status.toString}
         where todo_id = ${dto.id}
       """.update.run.transact(transactor) *> ().pure[F]

  def deleteBy(id: TodoId): F[Unit] =
    sql"""
         delete from sample_db.todo
         where todo_id = $id
       """.update.run.transact(transactor) *> ().pure[F]
}
