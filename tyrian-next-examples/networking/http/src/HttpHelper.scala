package example

import cats.effect.IO
import tyrian.next.*
import tyrian.http.*

object HttpHelper:

  /** HTTP requests in Tyrian Next use actions to perform asynchronous operations. This function
    * demonstrates how to make HTTP requests and handle responses.
    */
//```scala
  def getRandomGif(topic: String): Action =
    val url = s"https://api.giphy.com/v1/gifs/random?api_key=dc6zaTOxFJmzC&tag=$topic"

    val httpCmd: tyrian.Cmd[IO, HttpEvent] = Http.send(Request.get(url), HttpEvent.fromHttpResponse)

    Action(httpCmd)
//```
