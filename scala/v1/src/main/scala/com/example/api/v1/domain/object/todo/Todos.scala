package com.example.api.v1.domain.`object`.todo

import cats.Monoid
import cats.implicits._

case class Todos(toList: List[Todo]) extends AnyVal

object Todos {
  implicit val todoMonoid: Monoid[Todos] = Monoid[List[Todo]].imap(Todos(_))(_.toList)
}
