package example

import tyrian.Html.*
import tyrian.SVG.*
import tyrian.next.*
import scalajs.js

object Clock:

  /** The clock face is rendered as an SVG with a moving second hand. We calculate the second hand
    * position using trigonometry, converting the current seconds (0-59) into radians for the
    * rotation angle.
    */
//```scala
  def view(currentTime: js.Date): HtmlFragment =
    val angle = currentTime.getSeconds() * 2 * math.Pi / 60 - math.Pi / 2
    val handX = 50 + 40 * math.cos(angle)
    val handY = 50 + 40 * math.sin(angle)

    HtmlFragment(
      svg(viewBox := "0, 0, 100, 100", width := "300px")(
        circle(
          cx   := "50",
          cy   := "50",
          r    := "45",
          fill := "#0B79CE"
        ),
        line(
          x1     := "50",
          y1     := "50",
          x2     := handX.toString,
          y2     := handY.toString,
          stroke := "#023963"
        )
      )
    )
//```
