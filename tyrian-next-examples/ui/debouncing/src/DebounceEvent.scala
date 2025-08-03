package example

import tyrian.next.*

enum DebounceEvent extends GlobalMsg:
  case UpdateValue(newValue: String)
  case TimePassed
