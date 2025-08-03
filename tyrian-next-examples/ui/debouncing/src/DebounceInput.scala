package example

import tyrian.Html.*
import tyrian.next.*

/** A stateful debounced input component that manages its own debouncing logic. This demonstrates
  * proper encapsulation of stateful behavior in Tyrian Next.
  */
final case class DebounceInput(displayText: String, debouncer: Option[(String, Int)]):

  /** Debouncing logic encapsulated within the component. When a new value comes in, we start the
    * debounce timer. The timer countdown is handled by regular tick events from a watcher.
    */
//```scala
  def update: GlobalMsg => Outcome[DebounceInput] =
    case msg: DebounceEvent => handleDebounceEvent(msg)
    case _                  => Outcome(this)

  private def handleDebounceEvent(event: DebounceEvent): Outcome[DebounceInput] = event match
    case DebounceEvent.UpdateValue(v) =>
      Outcome(this.copy(debouncer = Some(v, DebounceInput.DebouncingMillis)))

    case DebounceEvent.TimePassed =>
      debouncer match
        case Some((v, time)) if time <= 0 =>
          Outcome(this.copy(displayText = v, debouncer = None))

        case Some((v, time)) =>
          Outcome(this.copy(debouncer = Some((v, time - DebounceInput.TickInterval))))

        case None =>
          Outcome(this)
//```

  def view: HtmlFragment =
    HtmlFragment(
      div(
        input(
          placeholder := "Debounced input",
          onInput(DebounceEvent.UpdateValue(_)),
          `value` := debouncer.map(_._1).getOrElse(displayText),
          myStyle
        ),
        div(myStyle)(text(displayText))
      )
    )

  private val myStyle =
    styles(
      "width"      -> "100%",
      "height"     -> "40px",
      "padding"    -> "10px 0",
      "font-size"  -> "2em",
      "text-align" -> "center"
    )

object DebounceInput:
  val DebouncingMillis: Int = 500
  val TickInterval: Int     = 100

  val init: DebounceInput = DebounceInput("", None)
