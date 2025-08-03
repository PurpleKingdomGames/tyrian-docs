package example

import tyrian.next.*
import scalajs.js

final case class Model(currentTime: js.Date):

  def update: GlobalMsg => Outcome[Model] =
    case ClockEvent.Tick(newTime) =>
      Outcome(this.copy(currentTime = newTime))

    case AppEvent.NoOp =>
      Outcome(this)

    case _ =>
      Outcome(this)

  def view: HtmlFragment =
    Clock.view(currentTime)

object Model:
  val init: Model =
    Model(new js.Date())
