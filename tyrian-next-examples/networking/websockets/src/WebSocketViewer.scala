package example

import tyrian.Html.*
import tyrian.next.*

object WebSocketViewer:

  def view(model: Model): HtmlFragment =
    HtmlFragment(
      div(
        model.echoSocket.connectDisconnectButton,
        p(button(onClick(SocketMsg.ToSocket("Hello!")))("send")),
        p("Log:"),
        p(model.log.flatMap(msg => List(text(msg), br)))
      )
    )
