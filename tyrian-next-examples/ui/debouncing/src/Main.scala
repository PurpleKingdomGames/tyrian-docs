package example

import tyrian.*
import tyrian.next.*
import scala.concurrent.duration.*

import scala.scalajs.js.annotation.*

@JSExportTopLevel("TyrianApp")
object Main extends TyrianNext[Model]:

  def router: Location => GlobalMsg =
    Routing.none(AppEvent.NoOp)

  def init(flags: Map[String, String]): Outcome[Model] =
    Outcome(Model.init)

  def update(model: Model): GlobalMsg => Outcome[Model] =
    case e =>
      model.update(e)

  def view(model: Model): HtmlRoot =
    HtmlRoot(model.view)

  /** Conditional watchers based on model state. The timer only runs when there's a pending debounce
    * operation, demonstrating dynamic watcher management.
    */
//```scala
  def watchers(model: Model): Batch[Watcher] =
    model.debounceInput.debouncer match
      case Some(_) =>
        Batch(
          Watcher.every(100.millis, _ => DebounceEvent.TimePassed)
        )
      case None =>
        Batch.empty
//```
