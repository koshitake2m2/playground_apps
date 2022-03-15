package com.example.api.v1.presentation

import cats.effect._
import cats.implicits._
import com.example.api.v1.infrastructure.dao.{DatabaseConfig, DoobieTodoDao}
import com.example.api.v1.infrastructure.repository.TodoRepositoryImpl
import com.example.api.v1.usecase._
import doobie.hikari.HikariTransactor
import io.circe.config.parser
import org.http4s.blaze.server._
import org.http4s.implicits._
import org.http4s.server.{Router, Server}
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

import scala.concurrent.ExecutionContext.global

object WebServer extends IOApp {

  lazy val port = 8011
  lazy val host = "localhost"
  implicit def unsafeLogger[F[_]: Sync]: Logger[F] = Slf4jLogger.getLogger[F]

  def run(args: List[String]): IO[ExitCode] =
    dependencies[IO]
      .use { _ =>
        IO.never
      }
      .as(ExitCode.Success)

  def databaseConfigResource[F[_]: MonadCancelThrow]: Resource[F, DatabaseConfig] =
    Resource.make(parser.decodePathF[F, DatabaseConfig]("database"))(_ => ().pure[F])

  def transactorResource[F[_]: Async]: Resource[F, HikariTransactor[F]] =
    databaseConfigResource.flatMap(_.transactor[F])

  def dependencies[F[_]: Async: MonadCancelThrow]: Resource[F, Server] = for {
    transactor <- transactorResource

    todoDao = new DoobieTodoDao[F](transactor)
    todoRepository = new TodoRepositoryImpl[F](todoDao)
    createTodo = new CreateTodo(todoRepository)
    getAllTodos = new GetAllTodos(todoRepository)
    getTodoById = new GetTodoById(todoRepository)
    updateTodo = new UpdateTodo(todoRepository)
    deleteTodoById = new DeleteTodoById(todoRepository)
    todoRoutes = new TodoRoutes[F](
      createTodo,
      deleteTodoById,
      getAllTodos,
      getTodoById,
      updateTodo
    )

    httpApp = Router(
      "/api/v1/todos" -> todoRoutes.of
    ).orNotFound
    server <- BlazeServerBuilder[F]
      .withExecutionContext(global)
      .bindHttp(port, host)
      .withHttpApp(httpApp)
      .resource
  } yield server
}
