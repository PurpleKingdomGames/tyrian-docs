package example

import tyrian.Html.*
import tyrian.next.*

/** A stateful text field component that reverses input text. This demonstrates proper component
  * encapsulation in Tyrian Next.
  */
final case class TextField(content: String):

  def update: GlobalMsg => Outcome[TextField] =
    case FieldEvent.NewContent(newContent) =>
      Outcome(this.copy(content = newContent))
    case _ =>
      Outcome(this)

//```scala
  def view: HtmlFragment =
    HtmlFragment(
      div(
        input(
          placeholder := "Text to reverse",
          onInput(s => FieldEvent.NewContent(s)),
          value := content,
          myStyle
        ),
        div(myStyle)(text(content.reverse))
      )
    )
//```

  private val myStyle =
    styles(
      "width"      -> "100%",
      "height"     -> "40px",
      "padding"    -> "10px 0",
      "font-size"  -> "2em",
      "text-align" -> "center"
    )

object TextField:
  val init: TextField = TextField("")
