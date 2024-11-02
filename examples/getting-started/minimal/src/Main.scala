package example

import cats.effect.IO
import tyrian.Html.*
import tyrian.*

import scala.scalajs.js.annotation.*

@JSExportTopLevel("TyrianApp")
object Counter extends TyrianIOApp[Msg, Model]:

  def router: Location => Msg = Routing.none(Msg.NoOp)

  def init(flags: Map[String, String]): (Model, Cmd[IO, Msg]) =
    (Model(), Cmd.None)

  def update(model: Model): Msg => (Model, Cmd[IO, Msg]) =
    case Msg.NoOp => (model, Cmd.None)

  def view(model: Model): Html[Msg] =
    div("Hello, Tyrian!")

  def subscriptions(model: Model): Sub[IO, Msg] =
    Sub.None

final case class Model()

enum Msg:
  case NoOp
