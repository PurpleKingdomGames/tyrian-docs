# Styles

There are lots of ways to make your Tyrian apps look nicer, see below for an example of [Bootstrap](https://getbootstrap.com/docs/3.4/css/), but there are many others you could try using the same principles, such as [TailwindCSS](https://tailwindcss.com/docs/installation).

## Bootstrap

Install [sass css](https://sass-lang.com/) and [Bootstrap](https://getbootstrap.com/docs/3.4/css/) in your project:

```
npm install sass
npm install bootstrap
```

Add a sass CSS file like the one below:

```css
@import "../node_modules/bootstrap/scss/bootstrap";

$font-stack: Helvetica, sans-serif;
$primary-color: #333;

body {
  font: 3em $font-stack;
  color: $primary-color;
}
```

Refer to [your bundler's documentation](https://parceljs.org/languages/sass/) for how to include it in your site build pipeline. For example, you may need to include a link to the file in the header of your HTML page:

`<link rel="stylesheet" href="./scss/custom.scss">`

Make use of the imported / available styles in your Scala based HTML:

```scala
  def view(model: Model): Html[Msg] =
    div(`class` := "container")(
      div(`class` := "row")(
        div(`class` := "col bodyText", styles("text-align" -> "right"))(
          button(onClick(Msg.Decrement))(text("-"))
        ),
        div(`class` := "col bodyText", styles("text-align" -> "center"))(
          text(model.toString)
        ),
        div(`class` := "col bodyText", styles("text-align" -> "left"))(
          button(onClick(Msg.Increment))(text("+"))
        )
      )
    )
```
