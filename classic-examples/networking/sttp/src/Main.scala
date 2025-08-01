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

/** ## Example: Using sttp HTTP client with Tyrian
  *
  * This example demonstrates how to integrate the sttp HTTP client into a Tyrian application. It
  * shows how to:
  *   - Fetch the user's public IP address and current time data from external APIs.
  *   - Manage asynchronous effects and loading state using Tyrian's Cmd and message system.
  *   - Display loading indicators, error messages, and fetched data in a reactive UI.
  *   - Sequence UI state transitions (loading, success, error) in a functional, message-driven
  *     architecture.
  */
@JSExportTopLevel("TyrianApp")
object Main extends TyrianIOApp[Msg, Model]:

  def main(args: Array[String]): Unit =
    launch("myapp")

  def router: Location => Msg = Routing.none(Msg.NoOp)

  def init(flags: Map[String, String]): (Model, Cmd[IO, Msg]) =
    (Model.init, Cmd.None)

  def update(model: Model): Msg => (Model, Cmd[IO, Msg]) =
    case Msg.NoOp         => (model, Cmd.None)
    case Msg.StartLoading => (model.copy(loading = true), Cmd.None)
    case Msg.Update(time, ip) =>
      (
        model.copy(data = Some(time -> ip), error = None, loading = false),
        Cmd.None
      )
    case Msg.SetError(msg) =>
      (model.copy(error = Some(msg), loading = false), Cmd.None)
    case Msg.Refresh =>
      (model, SttpHelper.fetchTime[IO])

  def view(model: Model): Html[Msg] =
    val timeView: Html[Nothing] =
      if (model.loading) {
        div(cls := "clock-container", style := "position: relative;")(
          div(cls := "spinner")()
        )
      } else {
        model.data match {
          case None =>
            div(cls := "clock-container")(
              div(cls := "clock-loading")(
                span(
                  text(
                    model.error
                      .map(_.getMessage)
                      .getOrElse("No time data yet. Click Refresh.")
                  )
                )
              )
            )
          case Some((data, myIP)) =>
            div(cls := "clock-container")(
              div(
                div(cls := "clock-time")(
                  span(
                    text(
                      f"${data.hour}%02d:${data.minute}%02d:${data.seconds}%02d"
                    )
                  )
                ),
                div(cls := "clock-date")(
                  span(
                    text(
                      f"${data.dayOfWeek}, ${data.year}-${data.month}%02d-${data.day}%02d"
                    )
                  )
                ),
                div(cls := "clock-tz")(
                  span(text(s"Time Zone: ${data.timeZone}"))
                ),
                div(cls := "clock-ip")(span(text(s"IP Address: ${myIP.ip}")))
              )
            )
        }
      }

    val styles =
      Html.raw("style") {
        """
        |body {
        |  margin: 0;
        |  padding: 0;
        |  font-family: 'Inter', 'Segoe UI', Arial, sans-serif;
        |}
        |.main-bg {
        |  display: flex;
        |  flex-direction: column;
        |  align-items: center;
        |  justify-content: center;
        |  min-height: 100vh;
        |  background: linear-gradient(135deg, #232526 0%, #414345 100%);
        |}
        |.clock-container {
        |  display: flex;
        |  flex-direction: column;
        |  align-items: center;
        |  justify-content: center;
        |  width: 350px;
        |  height: 250px;
        |  padding: 1.5rem;
        |  background: #181c20;
        |  border-radius: 1rem;
        |  box-shadow: 0 2px 16px rgba(0,0,0,0.1);
        |  margin: 2rem auto;
        |  color: #f8f8f8;
        |  position: relative;
        |}
        |.clock-loading {
        |  font-size: 1.5rem;
        |  color: #888;
        |  margin-bottom: 1rem;
        |}
        |.clock-time {
        |  font-family: 'SF Mono', 'Menlo', 'Monaco', 'Consolas', monospace;
        |  font-size: 3rem;
        |  letter-spacing: 0.1em;
        |  margin-bottom: 0.5rem;
        |}
        |.clock-date {
        |  font-size: 1.2rem;
        |  margin-bottom: 0.5rem;
        |}
        |.clock-tz {
        |  font-size: 1rem;
        |  margin-bottom: 0.5rem;
        |  color: #a0e7e5;
        |}
        |.clock-ip {
        |  font-size: 1rem;
        |  margin-bottom: 0.5rem;
        |  color: #ffd166;
        |}
        |.btn {
        |  margin-top: 2rem;
        |  padding: 0.75rem 2rem;
        |  font-size: 1.1rem;
        |  border-radius: 0.5rem;
        |  background: #00b4d8;
        |  color: #fff;
        |  border: none;
        |  cursor: pointer;
        |  transition: background 0.2s, box-shadow 0.2s, transform 0.1s;
        |}
        |.btn:hover {
        |  background: #48cae4;
        |  box-shadow: 0 4px 16px rgba(0, 180, 216, 0.15);
        |}
        |.btn:active {
        |  background: #0096c7;
        |  transform: scale(0.96);
        |  box-shadow: 0 2px 8px rgba(0, 150, 199, 0.18);
        |}
        |.spinner {
        |  width: 48px;
        |  height: 48px;
        |  border: 5px solid #e0e0e0;
        |  border-top: 5px solid #00b4d8;
        |  border-radius: 50%;
        |  animation: spin 1s linear infinite;
        |  z-index: 11;
        |}
        |@keyframes spin {
        |  0% { transform: rotate(0deg); }
        |  100% { transform: rotate(360deg); }
        |}
        |""".stripMargin.trim
      }

    div(cls := "main-bg")(
      styles,
      timeView,
      button(onClick(Msg.Refresh), cls := "btn")(text("Refresh"))
    )

  def subscriptions(model: Model): Sub[IO, Msg] =
    Sub.None

final case class TimeData(
    dayOfWeek: String,
    timeZone: String,
    year: Int,
    month: Int,
    day: Int,
    hour: Int,
    minute: Int,
    seconds: Int
)
final case class MyIP(ip: String)
final case class Model(
    data: Option[(TimeData, MyIP)],
    error: Option[Throwable],
    loading: Boolean
)

object Model:
  val init: Model =
    Model(None, None, false)

enum Msg:
  case NoOp
  case Update(time: TimeData, ip: MyIP)
  case SetError(error: Throwable)
  case Refresh
  case StartLoading

object SttpHelper:

  private val TimeAPIDefaultIP = MyIP("237.71.232.203")

  def fetchTime[F[_]: Async]: Cmd[F, Msg] = {
    val backend = FetchCatsBackend[F]()

    val request1 = basicRequest
      .get(uri"https://api.ipify.org/?format=json")
      .response(asJson[MyIP])

    def request2(ipAddress: String) = basicRequest
      .get(uri"https://timeapi.io/api/time/current/ip?ipAddress=$ipAddress")
      .response(asJson[TimeData])

    val httpCalls = for {
      response1 <- backend.send(request1).map(_.body).map {
        case Left(_)     => TimeAPIDefaultIP
        case Right(myIP) => myIP
      }

      ipAddress = response1.ip
      response2 <- backend
        .send(request2(ipAddress))
        .map(_.body)
        .handleError(e => Left(e))

    } yield response2 match {
      case Left(error) => Msg.SetError(error)
      case Right(time) => Msg.Update(time, response1)
    }

    Cmd.emit(Msg.StartLoading) |+| Cmd.Run(httpCalls)
  }
