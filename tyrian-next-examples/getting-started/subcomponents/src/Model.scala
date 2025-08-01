package example

import tyrian.*
import tyrian.next.*

final case class Model(counters: CounterManager):

  def update: GlobalMsg => Outcome[Model] =
    case AppEvent.NoOp =>
      Outcome(this)

    case e =>
      counters.update(e).map: cs =>
        this.copy(
          counters = cs
        )

  def view: HtmlFragment =
      counters.view

object Model:
  val init: Model =
    Model(CounterManager.initial)
