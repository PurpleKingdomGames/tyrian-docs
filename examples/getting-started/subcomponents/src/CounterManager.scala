package example

import tyrian.*
import tyrian.ui.*

final case class CounterManager(counters: Batch[Counter])(using Theme):

  def update: GlobalMsg => Result[CounterManager] =
    case CounterManagerEvent.Modify(index, msg) =>
      val cs = counters.zipWithIndex.map { case (c, i) =>
        if i == index then c.update(msg) else c
      }

      Result(this.copy(counters = cs))

    case CounterManagerEvent.Insert =>
      Result(
        this.copy(
          counters = Counter.initial :: counters
        )
      )

    case CounterManagerEvent.Remove =>
      Result(this.copy(counters = counters.drop(1)))

    case _ =>
      Result(this)

  def view: HtmlFragment =
    HtmlFragment(
      Column(
        Batch(
          Row(
            Button("remove", CounterManagerEvent.Remove),
            Button("insert", CounterManagerEvent.Insert)
          ).withSpacing(Spacing.comfortable)
        ) ++
          counters.zipWithIndex.map { case (c, i) =>
            c.view(i)
          }
      ).withSpacing(Spacing.comfortable)
    )

object CounterManager:
  def initial(using Theme): CounterManager =
    CounterManager(Batch.empty)

enum CounterManagerEvent extends GlobalMsg:
  case Modify(index: Int, msg: CounterEvent)
  case Insert
  case Remove
