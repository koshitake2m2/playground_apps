package com.example.api.v1.infrastructure.dao

import cats.effect.{Async, Resource}
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

case class DatabaseConfig(
  driver: String,
  url: String,
  user: String,
  password: String,
  connectionPoolSize: Int
) {
  def transactor[F[_]: Async]: Resource[F, HikariTransactor[F]] =
    for {
      connectEC <- ExecutionContexts.fixedThreadPool[F](connectionPoolSize)
      transactor <-
        HikariTransactor
          .newHikariTransactor[F](
            driver,
            url,
            user,
            password,
            connectEC
          )
    } yield transactor
}

object DatabaseConfig {
  implicit val dataBaseDecoder: Decoder[DatabaseConfig] = deriveDecoder
}
