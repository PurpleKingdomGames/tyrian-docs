package example

import cats.syntax.either.*
import io.circe.parser.*
import tyrian.next.*
import tyrian.http.*

enum HttpEvent extends GlobalMsg:
  case MorePlease
  case NewGif(result: String)
  case GifError(error: String)

object HttpEvent:
  def fromHttpResponse: tyrian.http.Decoder[HttpEvent] =
    tyrian.http.Decoder[HttpEvent](onResponse, onError)

  private val onResponse: Response => HttpEvent = { response =>
    val deserialised =
      parse(response.body)
        .leftMap(_.message)
        .flatMap {
          _.hcursor
            .downField("data")
            .downField("images")
            .downField("downsized_medium")
            .get[String]("url")
            .toOption
            .toRight("wrong json format")
        }

    deserialised match
      case Left(e)  => HttpEvent.GifError(e)
      case Right(r) => HttpEvent.NewGif(r)
  }

  private val onError: tyrian.http.HttpError => HttpEvent =
    e => HttpEvent.GifError(e.toString)
