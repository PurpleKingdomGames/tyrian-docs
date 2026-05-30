package example

import tyrian.classic.*

object Main {
  def main(args: Array[String]): Unit =
    TyrianIOApp.onLoad(
      "CounterApp" -> CounterApp,
      "ChatApp"    -> ChatApp
    )
}
