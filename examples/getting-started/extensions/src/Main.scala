package example

import tyrian.*
import tyrian.ui.*

import scala.scalajs.js.annotation.*

@JSExportTopLevel("TyrianApp")
object Main extends App[Unit, Model]:

  given Theme = Theme.default

  def router: Location => GlobalMsg = Routing.none(AppMsg.NoOp)

  def init(flags: Map[String, String]): Result[Model] =
    Result(Model())

  def update(model: Model): GlobalMsg => Result[Model] =
    case e: AppMsg =>
      AppMsg.handle(model)(e)

    case _ =>
      Result(model)

  def view(model: Model): HtmlRoot =
    HtmlRoot.div(
      HtmlFragment(
        Column(
          TextBlock("Hello, Tyrian!"),
          Placeholder(MarkerId("clock"))
        ).withSpacing(Spacing.comfortable)
      )
    )

  def watchers(model: Model): Batch[Watcher] =
    Batch.empty

  def extensions(flags: Map[String, String], model: Model): Set[Extension[Unit, HtmlFragment]] =
    Set(
      CustomExtension()
    )

final case class Model()

enum AppMsg extends GlobalMsg:
  case NoOp
  case Log(msg: String)

object AppMsg:

  def handle(model: Model): AppMsg => Result[Model] =
    case AppMsg.Log(msg) =>
      Result(model)
        .log(msg)

    case AppMsg.NoOp =>
      Result(model)
