package example

import tyrian.next.*

enum SocketMsg extends GlobalMsg:
  case FromSocket(message: String)
  case ToSocket(message: String)
  case WebSocketStatus(status: EchoSocket.Status)
