package example

import tyrian.Html.*
import tyrian.*
import tyrian.next.*

import scala.scalajs.js.annotation.*

/** The main thing to note is that the use of `Outcome`, `Batch`, `Action`, and `Watchers`.
  *
  *   - `Outcome` replaces `(model, Cmd)`
  *   - `Batch` is used as a `List` replacement. It isn't quite as friendly as list, but it is more
  *     performant for our needs.
  *   - `Action` replaces `Cmd` (to avoid ambiguous terms), but you can convert from one to the
  *     other, so you can use `Cmd`s in `Action`s.
  *   - `Watcher` replaces `Sub` (to avoid ambiguous terms), but you can convert from one to the
  *     other, so you can use `Sub`s in `Watcher`s.
  */
// ```scala
@JSExportTopLevel("TyrianApp")
object Main extends TyrianNext[Model]:

  def router: Location => GlobalMsg =
    Routing.none(NoOp)

  def init(flags: Map[String, String]): Outcome[Model] =
    Outcome(Model())

  def update(model: Model): GlobalMsg => Outcome[Model] =
    case NoOp =>
      Outcome(model)

    case _ =>
      Outcome(model)

  def view(model: Model): HtmlRoot =
    HtmlRoot(
      HtmlFragment(
        p("Hello, Tyrian!")
      )
    )

  def watchers(model: Model): Batch[Watcher] =
    Batch.empty
// ```

final case class Model()

case object NoOp extends GlobalMsg
