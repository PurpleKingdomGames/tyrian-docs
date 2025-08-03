package example

import cats.effect.IO
import tyrian.next.*
import tyrian.cmds.Logger

final case class Model(echoSocket: EchoSocket, log: List[String]):

  def update: GlobalMsg => Outcome[Model] =
    case msg: SocketMsg => handleSocketMsg(msg)
    case _              => Outcome(this)

  private def handleSocketMsg(msg: SocketMsg): Outcome[Model] = msg match
    case SocketMsg.WebSocketStatus(status) =>
      val (nextWS, actions) = echoSocket.update(status)
      Outcome(this.copy(echoSocket = nextWS)).addActions(actions*)

    case SocketMsg.FromSocket(message) =>
      val logAction = Action(Logger.info[IO]("Got: " + message))
      Outcome(this.copy(log = message :: log)).addActions(logAction)

    case SocketMsg.ToSocket(message) =>
      val logAction     = Action(Logger.info[IO]("Sent: " + message))
      val publishAction = echoSocket.publish(message)
      Outcome(this).addActions(logAction, publishAction)

  def view: HtmlFragment =
    WebSocketViewer.view(this)

object Model:
  val init: Model =
    Model(EchoSocket.init, Nil)
