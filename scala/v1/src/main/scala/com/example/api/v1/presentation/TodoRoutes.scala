package com.example.api.v1.presentation

import cats.MonadThrow
import cats.data.Validated
import cats.effect._
import cats.implicits._
import com.example.api.v1.domain.`object`.todo.TodoId
import com.example.api.v1.presentation.request.{CreateTodoRequestBody, UpdateTodoRequestBody, ValidationErrorMessage}
import com.example.api.v1.presentation.response.{TodoResponseBody, TodosResponseBody}
import com.example.api.v1.usecase._
import io.circe.generic.auto._
import io.circe.literal._
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.typelevel.log4cats.Logger

class TodoRoutes[F[_]: MonadThrow: Concurrent: Logger](
  createTodo: CreateTodo[F],
  deleteTodoById: DeleteTodoById[F],
  getAllTodos: GetAllTodos[F],
  getTodoById: GetTodoById[F],
  updateTodo: UpdateTodo[F]
) extends Http4sDsl[F] {

  def of: HttpRoutes[F] = HttpRoutes
    .of[F] {

      /** 全てのTodoを取得する.
        *
        * {{{
        *   curl -X GET localhost:8080/api/v1/todos
        * }}}
        */
      case GET -> Root =>
        for {
          todos <- getAllTodos.apply
          responseBody = TodosResponseBody.of(todos)
          response <- Ok(responseBody.asJson)
        } yield response

      /** Todoを追加する.
        *
        * {{{
        *   curl -X POST -H "Content-Type: application/json" -d '{"title":"title4", "status":"waiting"}' localhost:8080/api/v1/todos
        * }}}
        */
      case request @ POST -> Root =>
        for {
          requestBody <- request.as[CreateTodoRequestBody]
          okOrErrors <- requestBody.validate match {
            case Right(newTodo) =>
              createTodo.apply(newTodo).map(Right(_)) <* Logger[F].info("Created todo.")
            case Left(errorResponse) =>
              Left(errorResponse).pure[F]
          }
          response <- okOrErrors match {
            case Right(_) => Ok()
            case Left(errorResponse) => BadRequest(errorResponse.asJson.dropNullValues)
          }
        } yield response

      /** 指定したIDのTodoを取得する.
        *
        * {{{
        *   curl -X GET localhost:8080/api/v1/todos/1
        * }}}
        */
      case GET -> Root / IntVar(todoId) =>
        getTodoById(TodoId(todoId))
          .flatMap {
            case Some(todo) =>
              val responseBody = TodoResponseBody.of(todo)
              Ok(responseBody.asJson)
            case None => NotFound(ValidationErrorMessage.TodoId.NotFound(todoId).toString)
          }

      /** 指定したIDのTodoを更新する.
        *
        * {{{
        *   curl -X PUT -H "Content-Type: application/json" -d '{"title":"title4", "status":"doing"}' localhost:8080/api/v1/todos
        * }}}
        */
      case request @ PUT -> Root / IntVar(todoId) =>
        for {
          requestBody <- request.as[UpdateTodoRequestBody]
          okOrErrors <- requestBody.validateWith(todoId) match {
            case Validated.Valid(todo) =>
              // NOTE: 存在チェックはしない.
              updateTodo.apply(todo).map(Right(_)) <* Logger[F].info(s"Update todo. todoId=$todoId")
            case Validated.Invalid(errors) =>
              Left(errors).pure[F]
          }
          response <- okOrErrors match {
            case Right(_) => Ok()
            case Left(errors) => BadRequest(errors.toList.map(_.toString))
          }
        } yield response

      /** 指定したIDのTodoを削除する.
        *
        * {{{
        *   curl -X DELETE localhost:8080/api/v1/todos/4
        * }}}
        */
      case DELETE -> Root / IntVar(todoId) =>
        // NOTE: 存在チェックはしない.
        deleteTodoById(TodoId(todoId)) <* Logger[F].info(s"Deleted todo. todoId=$todoId.") >> Ok()
    }

}
