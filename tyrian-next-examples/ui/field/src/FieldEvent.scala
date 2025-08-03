package example

import tyrian.next.*

enum FieldEvent extends GlobalMsg:
  case NewContent(content: String)
