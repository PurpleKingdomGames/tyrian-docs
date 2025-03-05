package example

import tyrian.*

object Main {
  def main(args: Array[String]): Unit =
    TyrianIOApp.onLoad(
      "CounterApp" -> CounterApp,
      "ChatApp"    -> ChatApp
    )
}
