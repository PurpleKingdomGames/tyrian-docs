package example

import tyrian.next.*

final case class Model(textField: TextField):

  def update: GlobalMsg => Outcome[Model] =
    case e =>
      textField.update(e).map(tf => this.copy(textField = tf))

  def view: HtmlFragment =
    textField.view

object Model:
  val init: Model =
    Model(TextField.init)
