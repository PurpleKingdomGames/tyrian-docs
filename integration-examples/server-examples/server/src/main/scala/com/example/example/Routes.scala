package com.example.example

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.Header
import org.http4s.headers.`Content-Type`
import org.http4s.MediaType
import org.http4s.StaticFile
import fs2.io.file.Files

object Routes:

  def routes(ssr: SSR): HttpRoutes[IO] =
    val dsl = new Http4sDsl[IO] {}
    import dsl.*

    HttpRoutes.of[IO] {
      case GET -> Root =>
        Ok(tyrian.DOCTYPE + HomePage.page, `Content-Type`(MediaType.text.html))

      case request @ GET -> Root / "spa.js" =>
        val spa = fs2.io.file.Path(
          "."
        ) / "spa" / "target" / "scala-3.6.3" / "spa-opt" / "main.js"
        StaticFile.fromPath(spa.absolute, Some(request)).getOrElseF(NotFound(spa.absolute.toString))

      case GET -> Root / "ssr" / in =>
        for {
          out  <- ssr.render(SSR.Input(in))
          resp <- Ok(out.toHtml, `Content-Type`(MediaType.text.html))
        } yield resp

      case GET -> Root / "ssr" =>
        for {
          out  <- ssr.render
          resp <- Ok(out.toHtml, `Content-Type`(MediaType.text.html))
        } yield resp
    }
