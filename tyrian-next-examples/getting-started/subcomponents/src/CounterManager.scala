package example

import tyrian.Html.*
import tyrian.next.*

/** The `CounterManager` holds and manages a list of counters that are currently active. It is
  * following the Elm component pattern of having an `update` and `view` method.
  *
  * In this case, the `CounterManager` is interested in both it's own events _and_ the
  * `CounterEvent`s (which are wrapped in `CounterManagerEvent.Modify`), which it will delegate to
  * the counter instances. By making the `update` function accept `GlobalMsg`, we have to do a
  * little extra work matching on the event types, but it means that wiring this component in at the
  * next level up is trivially easy.
  *
  * `CounterManager`s view returns an `HtmlFragment` instead of HTML. This is because eventually we
  * need `HtmlFragment`s for the `HtmlRoot` instance to turn into HTML. `HtmlFragment`s are easy to
  * combine, and also support 'out of order' rendering, by placing `Marker` instances in the Html
  * and telling the fragment to add HTML chunks to the markers using the `insert` functions.
  */
// ```scala
final case class CounterManager(counters: List[Counter]):

  def update: GlobalMsg => Outcome[CounterManager] =
    case CounterManagerEvent.Modify(index, msg) =>
      val cs = counters.zipWithIndex.map { case (c, i) =>
        if i == index then c.update(msg) else c
      }

      Outcome(this.copy(counters = cs))

    case CounterManagerEvent.Insert =>
      Outcome(
        this.copy(
          counters = Counter.initial :: counters
        )
      )

    case CounterManagerEvent.Remove =>
      Outcome(this.copy(counters = counters.drop(1)))

    case _ =>
      Outcome(this)

  def view: HtmlFragment =
    HtmlFragment(
      div(
        List(
          button(onClick(CounterManagerEvent.Remove))(text("remove")),
          button(onClick(CounterManagerEvent.Insert))(text("insert"))
        ) ++
          counters.zipWithIndex.map { case (c, i) =>
            c.view.map(msg => CounterManagerEvent.Modify(i, msg))
          }
      )
    )
// ```

object CounterManager:
  val initial: CounterManager =
    CounterManager(Nil)

enum CounterManagerEvent extends GlobalMsg:
  case Modify(index: Int, msg: CounterEvent)
  case Insert
  case Remove
