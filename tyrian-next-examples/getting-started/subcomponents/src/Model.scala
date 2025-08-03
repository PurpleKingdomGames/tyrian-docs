package example

import tyrian.*
import tyrian.next.*

/** The `Model` is the start of our component setup. The aim is to have a tree of components that
  * can propagate and delegate to the components below them. The component pattern is not
  * formalised, because there are a few ways to do it, but the idea is simply that each component
  * will have some sort of `update` and `view` function the parent will call.
  *
  * If components need to talk to one another, there are again a few ways to do that. The idiomatic
  * solution is to have them talk via messages / events, but you can also resolve issues with
  * sub-components in the parent level. A physics engine, for example, would update all the
  * colliders under it, and then iteratively solve the collisions that occurred between them, before
  * calling the render function on each.
  */
// ```scala
final case class Model(counters: CounterManager):

  def update: GlobalMsg => Outcome[Model] =
    case e =>
      counters
        .update(e)
        .map { cs =>
          this.copy(
            counters = cs
          )
        }

  def view: HtmlFragment =
    counters.view
// ```

object Model:
  val init: Model =
    Model(CounterManager.initial)
