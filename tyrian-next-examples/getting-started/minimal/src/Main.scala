package example

import tyrian.Html.*
import tyrian.*
import tyrian.next.*

import scala.scalajs.js.annotation.*

@JSExportTopLevel("TyrianApp")
object Main extends TyrianNext[Model]:

  def router: Location => GlobalMsg =
    Routing.none(NoOp)

  def init(flags: Map[String, String]): Outcome[Model]=
    Outcome(Model())

  def update(model: Model): GlobalMsg => Outcome[Model] =
    case NoOp =>
      Outcome(model)

    case _ =>
      Outcome(model)

  def view(model: Model): HtmlRoot =
    HtmlRoot.div(
      HtmlFragment(
        p("Hello, Tyrian!")
      )
    )

  def watchers(model: Model): Batch[Watcher] =
    Batch.empty

final case class Model()

case object NoOp extends GlobalMsg
