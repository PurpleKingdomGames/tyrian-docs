package example

import tyrian.*
import tyrian.next.*

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

  /** WebSocket subscriptions in Tyrian Next use watchers to listen for events.
    */
//```scala
  def watchers(model: Model): Batch[Watcher] =
    val subscription = model.echoSocket.subscribe {
      case tyrian.websocket.WebSocketEvent.Error(errorMessage) =>
        SocketMsg.FromSocket(errorMessage)

      case tyrian.websocket.WebSocketEvent.Receive(message) =>
        SocketMsg.FromSocket(message)

      case tyrian.websocket.WebSocketEvent.Open =>
        SocketMsg.FromSocket("<no message - socket opened>")

      case tyrian.websocket.WebSocketEvent.Close(code, reason) =>
        SocketMsg.FromSocket(s"<socket closed> - code: $code, reason: $reason")

      case tyrian.websocket.WebSocketEvent.Heartbeat =>
        SocketMsg.ToSocket("<💓 heartbeat 💓>")
    }

    Batch(Watcher(subscription))
//```
