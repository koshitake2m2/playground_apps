package com.example.api.v1.presentation.request
import com.example.api.v1.domain

trait ValidationErrorMessage extends Throwable

object ValidationErrorMessage {
  object TodoId {
    case class NotFound(input: Int) extends ValidationErrorMessage {
      override def toString: String = s"Todo ID = $input は存在しません。"
    }
  }

  object TodoTitle {
    case object Empty extends ValidationErrorMessage {
      override def toString: String = s"Todoタイトルが空文字です。"
    }
    case class TooLong(input: String) extends ValidationErrorMessage {
      override def toString: String = s"$input の文字数が長過ぎます。${domain.`object`.todo.TodoTitle.maxLength}文字以下にしてください。"
    }
  }

  object TodoStatus {
    case class NotMatch(input: String) extends ValidationErrorMessage {
      override def toString: String = s"$input はTodoステータスとして不適です。"
    }
  }

}
