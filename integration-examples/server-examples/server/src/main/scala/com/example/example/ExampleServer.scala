package com.example.example

import cats.effect.{IO, Resource}
import cats.syntax.all.*
import com.comcast.ip4s.*
import fs2.Stream
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits.*
import org.http4s.server.middleware.Logger

object ExampleServer:

  def stream: Stream[IO, Nothing] = {
    for {
      client <- Stream.resource(EmberClientBuilder.default[IO].build)

      httpApp = Routes.routes(SSR.impl).orNotFound

      finalHttpApp = Logger.httpApp(true, true)(httpApp)

      exitCode <- Stream.resource(
        EmberServerBuilder
          .default[IO]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(finalHttpApp)
          .build >>
          Resource.eval(IO.never)
      )
    } yield exitCode
  }.drain
