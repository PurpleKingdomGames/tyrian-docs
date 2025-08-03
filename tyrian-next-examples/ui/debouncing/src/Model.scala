package example

import tyrian.next.*

final case class Model(debounceInput: DebounceInput):

  def update: GlobalMsg => Outcome[Model] =
    case e =>
      debounceInput.update(e).map(di => this.copy(debounceInput = di))

  def view: HtmlFragment =
    debounceInput.view

object Model:
  val init: Model =
    Model(DebounceInput.init)
