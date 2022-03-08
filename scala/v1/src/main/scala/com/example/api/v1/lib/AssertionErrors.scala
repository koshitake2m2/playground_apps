package com.example.api.v1.lib

import cats.Semigroup
import cats.data._
import cats.implicits._

/** 表明エラーのコレクションオブジェクト. */
case class AssertionErrors(underlying: NonEmptyChain[AssertionError]) extends Error

object AssertionErrors {
  implicit val assertionErrorsSemigroup: Semigroup[AssertionErrors] =
    Semigroup[NonEmptyChain[AssertionError]].imap(AssertionErrors(_))(_.underlying)

  def one(message: String): AssertionErrors = AssertionErrors(NonEmptyChain.one(new AssertionError(message)))
  def one(assertionError: AssertionError): AssertionErrors = AssertionErrors(NonEmptyChain.one(assertionError))
}
