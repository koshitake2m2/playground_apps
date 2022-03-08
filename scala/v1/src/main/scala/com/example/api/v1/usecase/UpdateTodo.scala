package com.example.api.v1.usecase

import cats.data.{Validated, ValidatedNec}
import cats.implicits._
import com.example.api.v1.domain.`object`.todo.Todo
import com.example.api.v1.domain.repository.TodoRepository

class UpdateTodo[F[_]](
  repository: TodoRepository[F]
) {
  def apply(todo: Todo): F[Unit] = repository.save(todo)
}

object UpdateTodo {
  case class Name(value: String) {}
  object Name {
    def ofValidated(value: String): ValidatedNec[String, Name] =
      Validated.condNec(value.nonEmpty, Name(value), "Name could not be blank.")
  }

  case class Age(value: Int)
  object Age {
    def ofValidated(value: Int): ValidatedNec[String, Age] =
      Validated.condNec(value >= 0, Age(value), "Age could not be negative.")
  }

  case class Person(name: Name, age: Age)
  object Person {
    def ofValidated(name: String, age: Int): ValidatedNec[String, Person] =
      (Name.ofValidated(name), Age.ofValidated(age)).mapN { case (n, a) => Person.apply(n, a) }
  }

}
