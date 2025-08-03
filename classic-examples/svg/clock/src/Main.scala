package example

import cats.effect.IO
import tyrian.Html.*
import tyrian.SVG.*
import tyrian.*

import scala.scalajs.js.annotation.*

import scalajs.js
import concurrent.duration.DurationInt

@JSExportTopLevel("TyrianApp")
object Main extends TyrianIOApp[Msg, Model]:

  def router: Location => Msg = Routing.none(Msg.NoOp)

  def init(flags: Map[String, String]): (Model, Cmd[IO, Msg]) =
    (new js.Date(), Cmd.None)

  /** The update function handles tick messages by simply updating the model with the new time.
    */
//```scala
  def update(model: Model): Msg => (Model, Cmd[IO, Msg]) =
    case Msg.Tick(newTime) => (newTime, Cmd.None)
    case Msg.NoOp          => (model, Cmd.None)
//```

  /** The view function renders an SVG clock face with a moving second hand.
    *
    * We calculate the angle of the second hand based on the current seconds (0-59), then compute
    * the X and Y coordinates for the tip of the hand using trigonometry.
    */
//```scala
  def view(model: Model): Html[Msg] =
    val angle = model.getSeconds() * 2 * math.Pi / 60 - math.Pi / 2
    val handX = 50 + 40 * math.cos(angle)
    val handY = 50 + 40 * math.sin(angle)

    svg(viewBox := "0, 0, 100, 100", width := "300px")(
      circle(
        cx   := "50",
        cy   := "50",
        r    := "45",
        fill := "#0B79CE"
      ),
      line(
        x1     := "50",
        y1     := "50",
        x2     := handX.toString,
        y2     := handY.toString,
        stroke := "#023963"
      )
    )
//```

  /** The subscription sends a tick message every second, providing the current time. This drives
    * the clock animation by continuously updating the model.
    */
//```scala
  def subscriptions(model: Model): Sub[IO, Msg] =
    Sub.every[IO](1.second, "clock-ticks").map(Msg.Tick.apply)
//```

type Model = js.Date

enum Msg:
  case Tick(newTime: js.Date)
  case NoOp
