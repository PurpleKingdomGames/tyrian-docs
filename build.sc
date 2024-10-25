import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`
import mill._
import mill.scalalib._
import mill.scalajslib._
import mill.scalajslib.api._

import $file.scripts.examplemodule

object examples extends mill.Module {

  object demos extends mill.Module {

    object counter extends examplemodule.ExampleModule

  }

}
