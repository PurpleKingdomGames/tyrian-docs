package example

import tyrian.*
import tyrian.Html.*
import tyrian.next.*

/** Each individual Counter is handled with a Counter component instance. The isntance is stateful
  * and lives in the CounterManager.
  *
  * The Counter component follows the typical Elm-component architecture of having an `update` and a
  * `view` method.
  *
  * Since the `Counter` `update` takes a `CounterEvent`, we can have exhaustive message checking.
  * The problem is that it means something higher up needs to pre-extract these message type for the
  * Counters (CounterManager in this case), which can make message / event wiring fragile.
  * 
  * The counters view is simple enough to return normal HTML.
  */
// ```scala
final case class Counter(value: Int):
  def update: CounterEvent => Counter =
    case CounterEvent.Increment =>
      this.copy(value = value + 1)

    case CounterEvent.Decrement =>
      this.copy(value = value - 1)

  def view: Html[CounterEvent] =
    div(
      button(onClick(CounterEvent.Decrement))(text("-")),
      div(text(value.toString)),
      button(onClick(CounterEvent.Increment))(text("+"))
    )
// ```

object Counter:

  val initial: Counter =
    Counter(0)

/** In Tyrian-Next, all messages extend the `GlobalMsg` type. This means that you lose absolute
  * exhaustive message type checking, but the advantage is that it now feels natural to keep the
  * messages types with the components they're related to.
  */
// ```scala
enum CounterEvent extends GlobalMsg:
  case Increment
  case Decrement
// ```
