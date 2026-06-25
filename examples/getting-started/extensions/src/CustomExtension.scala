package example

import tyrian.*
import tyrian.syntax.*

import tyrian.classic.Html.*
import tyrian.classic.SVG.*

final case class CustomExtension() extends Extension.Standard[HtmlFragment]:

  type ExtensionModel = CustomExtension.Model

  def id: ExtensionId =
    ExtensionId("custom extension")

  def init: Result[CustomExtension.Model] =
    Result(CustomExtension.Model(Seconds.zero))

  def update(model: CustomExtension.Model): GlobalMsg => Result[CustomExtension.Model] =
    case CustomExtension.Tick(newTime) =>
      Result(model.copy(time = newTime))
        .addGlobalMsgs(AppMsg.Log(s"tick [${newTime.toDouble}]"))

    case _ =>
      Result(model)

  def view(model: CustomExtension.Model): HtmlFragment =

    val angle = model.time.toDouble * 2 * math.Pi / 60 - math.Pi / 2
    val handX = 50 + 40 * math.cos(angle)
    val handY = 50 + 40 * math.sin(angle)

    val clockSVG =
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

    HtmlFragment.insert(MarkerId("clock"), clockSVG)

  def watchers(model: CustomExtension.Model): Batch[Watcher] =
    Batch(
      Watcher.fromDate(Seconds(1), dt => CustomExtension.Tick(Seconds(dt.getSeconds())))
    )

  def prepare(model: CustomExtension.Model): Unit =
    ()

  def teardown(model: CustomExtension.Model): Unit =
    ()

object CustomExtension:

  final case class Model(time: Seconds)

  final case class Tick(newTime: Seconds) extends GlobalMsg
