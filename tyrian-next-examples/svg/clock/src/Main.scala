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

  /** Watchers replace subscriptions in Tyrian Next. Here we use `Watcher.every` to generate tick
    * events with the current time every second.
    */
//```scala
  def watchers(model: Model): Batch[Watcher] =
    Batch(
      Watcher.every(1000.millis, ClockEvent.Tick.apply)
    )
//```
