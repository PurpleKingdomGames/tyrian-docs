package com.example.example

import cats.effect.IO
import tyrian.*
import tyrian.Html.*

trait SSR:
  def render(n: SSR.Input): IO[SSR.Output]
  def render: IO[SSR.Output]

object SSR:

  val styles  = style(CSS.`font-family`("Arial, Helvetica, sans-serif"))
  val topLine = p(b(text("HTML fragment rendered by Tyrian on the server.")))

  def impl: SSR = new SSR:
    def render(in: SSR.Input): IO[SSR.Output] =
      val html =
        div(styles)(
          topLine,
          p(text("Was sent the following: " + in.toString))
        ).render

      IO.pure(SSR.Output(html))

    def render: IO[SSR.Output] =
      val html = div(styles)(topLine).render
      IO.pure(SSR.Output(html))

  opaque type Input = String
  object Input:
    inline def apply(s: String): Input        = s
    extension (n: Input) def toString: String = n

  opaque type Output = String
  object Output:
    inline def apply(s: String): Output      = s
    extension (o: Output) def toHtml: String = o
