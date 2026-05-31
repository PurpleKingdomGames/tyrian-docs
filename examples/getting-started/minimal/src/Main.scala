package example

import tyrian.*
import tyrian.ui.*

import scala.scalajs.js.annotation.*

@JSExportTopLevel("TyrianApp")
object Main extends App[Unit, Model]:

  given Theme = Theme.default

  def router: Location => GlobalMsg = Routing.none(NoOp())

  def init(flags: Map[String, String]): Result[Model] =
    Result(Model())

  def update(model: Model): GlobalMsg => Result[Model] =
    case NoOp() =>
      Result(model)

    case _ =>
      Result(model)

  def view(model: Model): HtmlRoot =
    HtmlRoot.div(
      HtmlFragment(
        TextBlock("Hello, Tyrian!")
      )
    )

  def watchers(model: Model): Batch[Watcher] =
    Batch.empty

  def extensions(flags: Map[String, String], model: Model): Set[Extension[Unit, HtmlFragment]] =
    Set()

final case class Model()

final case class NoOp() extends GlobalMsg
