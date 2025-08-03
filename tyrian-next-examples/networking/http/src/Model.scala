package example

import tyrian.next.*

final case class Model(topic: String, gifUrl: String):

  def update: GlobalMsg => Outcome[Model] =
    case msg: HttpEvent => handleHttpEvent(msg)
    case _              => Outcome(this)

  private def handleHttpEvent(event: HttpEvent): Outcome[Model] = event match
    case HttpEvent.MorePlease =>
      Outcome(this).addActions(HttpHelper.getRandomGif(topic))

    case HttpEvent.NewGif(newUrl) =>
      Outcome(this.copy(gifUrl = newUrl))

    case HttpEvent.GifError(_) =>
      Outcome(this)

  def view: HtmlFragment =
    GifViewer.view(this)

object Model:
  val init: Model =
    Model("cats", "waiting.gif")
