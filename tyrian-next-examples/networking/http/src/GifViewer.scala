package example

import tyrian.Html.*
import tyrian.next.*

object GifViewer:

  def view(model: Model): HtmlFragment =
    HtmlFragment(
      div(
        h2(text(model.topic)),
        button(onClick(HttpEvent.MorePlease))(text("more please")),
        br,
        img(src := model.gifUrl)
      )
    )
