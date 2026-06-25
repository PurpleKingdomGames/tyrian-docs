# Extensions

Tyrian `Extension`s are the spiritual equivalent of Indigo's `SubSystem`s, and similarly, the purpose of extensions is primarily encapsulation and organisation.

An `Extension` is a mini-Tyrian app that is separate from the main app, communicating via `GlobalMsg`s, but mechanically composed into the complete view. The order of composition is the main tyrian app, then the extensions. View composition is handled using the `Placeholder(markerId)` UI element, and `HtmlFragment.insert(markerId, ...view elems... )`.

Extensions come in two forms:

## `Extension.Standard`

Standard extensions, compose either `HtmlFragment`s or `TerminalFragment`s, to output a view suitable for the current platform.

## `Extension.Graphical`

Graphical extensions supply a graphics context (if there is one) to the extension for special modes of rendering, such as (currently) WebGL or OpenGL.
