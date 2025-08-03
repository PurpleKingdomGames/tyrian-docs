package example

import cats.effect.IO
import tyrian.Html.*
import tyrian.next.*
import tyrian.websocket.*
import tyrian.cmds.Logger

final case class EchoSocket(socketUrl: String, socket: Option[WebSocket[IO]]):

  def connectDisconnectButton =
    if socket.isDefined then button(onClick(EchoSocket.Status.Disconnecting.asMsg))("Disconnect")
    else button(onClick(EchoSocket.Status.Connecting.asMsg))("Connect")

  /** WebSocket management in Tyrian Next uses actions instead of commands. This demonstrates the
    * pattern for handling WebSocket state changes.
    */
//```scala
  def update(status: EchoSocket.Status): (EchoSocket, List[Action]) =
    status match
      case EchoSocket.Status.ConnectionError(err) =>
        val logAction = Action(Logger.error[IO](s"Failed to open WebSocket connection: $err"))
        (this, List(logAction))

      case EchoSocket.Status.Connected(ws) =>
        (this.copy(socket = Some(ws)), Nil)

      case EchoSocket.Status.Connecting =>
        val connectCmd = WebSocket.connect[IO, SocketMsg](
          address = socketUrl,
          onOpenMessage = "Connect me!",
          keepAliveSettings = KeepAliveSettings.default
        ) {
          case WebSocketConnect.Error(err) =>
            EchoSocket.Status.ConnectionError(err).asMsg

          case WebSocketConnect.Socket(ws) =>
            EchoSocket.Status.Connected(ws).asMsg
        }

        (this, List(Action(connectCmd)))

      case EchoSocket.Status.Disconnecting =>
        val logAction = Action(Logger.info[IO]("Graceful shutdown of EchoSocket connection"))
        val actions = socket
          .map(_ => List(logAction))
          .getOrElse(List(logAction))

        (this.copy(socket = None), actions)

      case EchoSocket.Status.Disconnected =>
        val logAction = Action(Logger.info[IO]("WebSocket not connected yet"))
        (this, List(logAction))
//```

  def publish(message: String): Action =
    socket
      .map(ws => Action(ws.publish(message)))
      .getOrElse(Action(tyrian.Cmd.None))

  def subscribe(
      toMessage: tyrian.websocket.WebSocketEvent => SocketMsg
  ): tyrian.Sub[IO, SocketMsg] =
    socket.fold(tyrian.Sub.emit[IO, SocketMsg](EchoSocket.Status.Disconnected.asMsg)) {
      _.subscribe(toMessage)
    }

object EchoSocket:

  val init: EchoSocket =
    EchoSocket("ws://localhost:8080/wsecho", None)

  enum Status:
    case Connecting
    case Connected(ws: WebSocket[IO])
    case ConnectionError(msg: String)
    case Disconnecting
    case Disconnected

    def asMsg: SocketMsg = SocketMsg.WebSocketStatus(this)
