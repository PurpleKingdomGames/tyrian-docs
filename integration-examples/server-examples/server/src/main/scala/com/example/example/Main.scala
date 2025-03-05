package com.example.example

import cats.effect.{ExitCode, IOApp}

object Main extends IOApp:
  def run(args: List[String]) =
    ExampleServer.stream.compile.drain.as(ExitCode.Success)
