package example

import cats.effect.IO
import tyrian.Html.*
import tyrian.*
import sttp.client4.impl.cats.FetchCatsBackend
import sttp.client4.*
import sttp.client4.circe.*
import io.circe.generic.auto.*
import cats.effect.kernel.Async
import cats.implicits.*

import scala.scalajs.js.annotation.*

@JSExportTopLevel("TyrianApp")
object Main extends TyrianIOApp[Msg, Model]:

  def router: Location => Msg = Routing.none(Msg.NoOp)

  def init(flags: Map[String, String]): (Model, Cmd[IO, Msg]) =
    (Model.init, Cmd.None)

  def update(model: Model): Msg => (Model, Cmd[IO, Msg]) =
    case Msg.NoOp         => (model, Cmd.None)
    case Msg.Update(time, ip) => (model.copy(data = Some(time -> ip)), Cmd.None)
    case Msg.Refresh =>
      (model, SttpHelper.fetchTime[IO])

  def view(model: Model): Html[Msg] =
    val timeView: Html[Nothing] = model.data match {
      case None =>
        div(cls := "clock-container")(
          div(cls := "clock-loading")(span(text("No time data yet. Click Refresh.")))
        )
      case Some((data, myIP)) =>
        div(cls := "clock-container")(
          div(
            div(cls := "clock-time")(span(text(f"${data.hour}%02d:${data.minute}%02d:${data.seconds}%02d"))),
            div(cls := "clock-date")(span(text(f"${data.dayOfWeek}, ${data.year}-${data.month}%02d-${data.day}%02d"))),
            div(cls := "clock-tz")(span(text(s"Time Zone: ${data.timeZone}"))),
            div(cls := "clock-ip")(span(text(s"IP Address: ${myIP.ip}")))
          )
        )
    }
    div(cls := "main-bg")(
      timeView,
      button(onClick(Msg.Refresh), cls := "btn")(text("Refresh"))
    )

  def subscriptions(model: Model): Sub[IO, Msg] =
    Sub.None


case class TimeData(dayOfWeek: String, timeZone: String, year: Int, month: Int, day: Int, hour: Int, minute: Int, seconds: Int)
case class MyIP(ip: String)
case class Model(data: Option[(TimeData, MyIP)])

object Model:
  val init: Model =
    Model(None)

enum Msg:
  case NoOp
  case Update(time: TimeData, ip: MyIP)
  case Refresh

object SttpHelper:

  def fetchTime[F[_]: Async]: Cmd[F, Msg] = {
    val backend   = FetchCatsBackend[F]()

    val request1 = basicRequest
    .get(uri"https://api.ipify.org/?format=json")
    .response(asJson[MyIP])

    def request2(ipAddress: String) = basicRequest
      .get(uri"https://timeapi.io/api/time/current/ip?ipAddress=$ipAddress")
      .response(asJson[TimeData])

    val httpCalls = for {
      response1 <- backend.send(request1).map(_.body).map{
        case Left(_) => MyIP("237.71.232.203") //Fallback IP Address
        case Right(myIP) => myIP
      }

      ipAddress = response1.ip
      response2 <- backend.send(request2(ipAddress)).map(_.body)
    } yield response2 match {
      case Left(_)     => Msg.NoOp
      case Right(time) => Msg.Update(time, response1)
    }

    Cmd.Run(httpCalls)
  }
