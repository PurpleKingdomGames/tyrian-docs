package example

import tyrian.*
import tyrian.ui.*

final case class Counter(value: Int):
  def update: CounterEvent => Counter =
    case CounterEvent.Increment =>
      this.copy(value = value + 1)

    case CounterEvent.Decrement =>
      this.copy(value = value - 1)

  def view(id: Int): UIElement[?, ?] =
    Row(
      Button("-", CounterManagerEvent.Modify(id, CounterEvent.Decrement)),
      TextBlock(value.toString()),
      Button("+", CounterManagerEvent.Modify(id, CounterEvent.Increment))
    ).withSpacing(Spacing.comfortable)

object Counter:

  val initial: Counter =
    Counter(0)

enum CounterEvent extends GlobalMsg:
  case Increment
  case Decrement
