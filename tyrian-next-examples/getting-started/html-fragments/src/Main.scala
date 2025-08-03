package example

import tyrian.Html.*
import tyrian.*
import tyrian.next.*
import tyrian.next.syntax.*

import scala.scalajs.js.annotation.*

/** In order to control the rendering we need to use markers, and markers have IDs. It's a good idea
  * to keep these as constants somewhere.
  */
// ```scala
object ViewMarkers:
  val A: MarkerId = MarkerId("a")
  val B: MarkerId = MarkerId("b")
  val C: MarkerId = MarkerId("c")
// ```

/** First we'll need a basic component. Since the component does almost nothing, this setup feels
  * like overkill in our use case, but later on we'll use it to demonstrate how it makes updates and
  * rendering easy to wire in.
  *
  * Notice that the `view` function is making an `HtmlFragment` using `HtmlFragment.insert`, this
  * tells the fragment that this HTML will be assigned to a marker somewhere later.
  */
// ```scala
final case class MyComponent(id: MarkerId):

  def update: GlobalMsg => Outcome[MyComponent] =
    _ => Outcome(this)

  def view: HtmlFragment =
    HtmlFragment.insert(
      id,
      p(s"Component: ${id.value}")
    )
// ```

/** The model also follows the component pattern, and mechanically updates and presents the
  * components. Nice and simple, but this pattern is useful because it allows the Model to manage
  * the components under it (whatever manage means, could be adding more, removing, finding or
  * altering, etc.).
  */
// ```scala
final case class Model(components: Batch[MyComponent]):
  def update: GlobalMsg => Outcome[Model] = e =>
    components.map(_.update(e)).sequence.map { updated =>
      this.copy(components = updated)
    }

  def view: Batch[HtmlFragment] =
    components.map(_.view)
// ```

@JSExportTopLevel("TyrianApp")
object Main extends TyrianNext[Model]:

  def router: Location => GlobalMsg =
    Routing.none(NoOp)

  /** When we initialise the model we'll add the components purposely out of order, for
    * demonstration purposes.
    */
// ```scala
  def init(flags: Map[String, String]): Outcome[Model] =
    Outcome(
      Model(
        Batch(
          MyComponent(ViewMarkers.A),
          MyComponent(ViewMarkers.C),
          MyComponent(ViewMarkers.B)
        )
      )
    )
// ```

  /** The main update function is simple delegation.
    */
// ```scala
  def update(model: Model): GlobalMsg => Outcome[Model] =
    case e => model.update(e)
// ```

  /** The main function does two things:
    *
    *   1. Sets up markers - these are placeholders for where we want the Html to end up. Notice
    *      that these are now in order. This is very simple structure, but markers can be deeply
    *      nested too.
    *
    * 2. It combines the fragment containing the markers with the result of calling view on the
    * model, and the view is constructed for us.
    */
// ```scala
  def view(model: Model): HtmlRoot =
    HtmlRoot(
      HtmlFragment(
        Marker(ViewMarkers.A),
        Marker(ViewMarkers.B),
        Marker(ViewMarkers.C)
      )
    ).addHtmlFragments(model.view)
// ```

  def watchers(model: Model): Batch[Watcher] =
    Batch.empty

case object NoOp extends GlobalMsg
