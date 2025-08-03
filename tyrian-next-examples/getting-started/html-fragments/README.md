# Html Fragments

This example is about 'Out of order' rendering.

In the classic Elm style, you construct the view HTML in one go, based on the model. That works, but comes with the drawback that you have to - in effect - render everything 'in order'. Or at least, you have to co-ordinate the construction so that everything turns up in the right place. This has the knock on effect of enticing the developer away from the idea of building encapsulated components.

What would be better, is if we could render all our components in any order (or the order that makes sense for the data they need), and then just mechanically glue them together, safe in the knowledge that the right fragment of HTML would turn up in the right part of the final DOM.

This is the function of `HtmlRoot` and `HtmlFragment`.

> `HtmlFragment` borrows from a similar notion in Indigo, called `SceneUpdateFragment`.

Note that using `Marker`s is _not_ the same as HTML templating. In Tyrian, an HTML template (i.e. HTML that needs to be hydrated with data), is just a function, e.g.:

```scala
def nameInHtml(name: String): Html[GlobalMsg] =
  p(name)
```