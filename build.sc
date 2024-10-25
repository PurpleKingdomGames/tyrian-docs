import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`
import mill._
import mill.scalalib._
import mill.scalajslib._
import mill.scalajslib.api._

import $file.scripts.shadermodule

import indigoplugin._

object examples extends mill.Module {

  object primitives extends mill.Module {

    object graphic extends shadermodule.ShaderModule {
      val indigoOptions: IndigoOptions =
        makeIndigoOptions("Graphic")
    }

  }

}
