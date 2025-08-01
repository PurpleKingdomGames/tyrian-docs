package example

import cats.effect.IO
import tyrian.Html.*
import tyrian.*

import scala.scalajs.js.annotation.*

/** To use Tyrian with Cats Effect, we need to bring in the tyrian-io library.
  *
  * ```
  * ivy"io.indigoengine::tyrian-io::x.y.z" // Mill
  * libraryDependencies += "io.indigoengine" %%% "tyrian-io" % "x.y.z" // SBT
  * ```
  *
  * Then we can bring in the `TyrianIOApp` trait to create our application.
  */
//```scala
@JSExportTopLevel("TyrianApp")
object Counter extends TyrianIOApp[Msg, Model]:
//```

  def router: Location => Msg = Routing.none(Msg.NoOp)

  def init(flags: Map[String, String]): (Model, Cmd[IO, Msg]) =
    (Model.init, Cmd.None)

  /** `Cmd`s and `Sub`s then make use of the `IO` type to perform side effects.
    */
  // ```scala
  def update(model: Model): Msg => (Model, Cmd[IO, Msg]) =
    case Msg.Increment => (model + 1, Cmd.None)
    case Msg.Decrement => (model - 1, Cmd.None)
    case Msg.NoOp      => (model, Cmd.None)
  // ```

  def view(model: Model): Html[Msg] =
    div(
      button(onClick(Msg.Decrement))("-"),
      div(model.toString),
      button(onClick(Msg.Increment))("+")
    )

  // ```scala
  def subscriptions(model: Model): Sub[IO, Msg] =
    Sub.None
  // ```

opaque type Model = Int
object Model:
  def init: Model = 0

  extension (i: Model)
    def +(other: Int): Model = i + other
    def -(other: Int): Model = i - other

enum Msg:
  case Increment, Decrement, NoOp
