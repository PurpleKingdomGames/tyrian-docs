package example

import tyrian.*
import tyrian.ui.Theme

final case class Model(counters: CounterManager):

  def update: GlobalMsg => Result[Model] =
    case AppMsg.NoOp =>
      Result(this)

    case e =>
      counters
        .update(e)
        .map: cs =>
          this.copy(
            counters = cs
          )

  def view: HtmlFragment =
    counters.view

object Model:
  def init(using Theme): Model =
    Model(CounterManager.initial)
