package example

import tyrian.*
import tyrian.ui.Theme

import scala.scalajs.js.annotation.*

@JSExportTopLevel("TyrianApp")
object Main extends App[Unit, Model]:

  given Theme = Theme.default

  def router: Location => GlobalMsg =
    Routing.none(AppMsg.NoOp)

  def init(flags: Map[String, String]): Result[Model] =
    Result(Model.init)

  def update(model: Model): GlobalMsg => Result[Model] =
    case e =>
      model.update(e)

  def view(model: Model): HtmlRoot =
    HtmlRoot.div(model.view)

  def watchers(model: Model): Batch[Watcher] =
    Batch.empty

  def extensions(flags: Map[String, String], model: Model): Set[Extension[Unit, HtmlFragment]] =
    Set()
