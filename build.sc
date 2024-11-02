import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`
import mill._
import mill.scalalib._
import mill.scalajslib._
import mill.scalajslib.api._

import $file.scripts.examplemodule

object examples extends mill.Module {

  object effects extends mill.Module {

    object `cats-effect` extends examplemodule.ExampleModule
    object fs2        extends examplemodule.ExampleModule
    object zio extends examplemodule.ExampleModule {
      override def ivyDeps =
        Agg(
          ivy"io.indigoengine::tyrian-zio::$tyrianVersion",
          ivy"dev.zio::zio-interop-cats::23.0.0.5"
        )
    }

  }

  object `getting-started` extends mill.Module {

    object minimal       extends examplemodule.ExampleModule
    object subcomponents extends examplemodule.ExampleModule

  }

  object networking extends mill.Module {

    object http extends examplemodule.ExampleModule {
      override def ivyDeps =
        super.ivyDeps() ++
          Agg(
            ivy"io.circe::circe-core::0.14.5",
            ivy"io.circe::circe-parser::0.14.5"
          )
    }
    object `http4s-dom` extends examplemodule.ExampleModule {
      override def ivyDeps =
        super.ivyDeps() ++
          Agg(
            ivy"org.http4s::http4s-dom::0.2.7",
            ivy"org.http4s::http4s-circe::0.23.18",
            ivy"io.circe::circe-core::0.14.5",
            ivy"io.circe::circe-parser::0.14.5",
            ivy"io.circe::circe-generic::0.14.5"
          )
    }
    object websockets extends examplemodule.ExampleModule

  }

  object svg extends mill.Module {

    object clock extends examplemodule.ExampleModule

  }

  object ui extends mill.Module {

    object debouncing extends examplemodule.ExampleModule
    object field      extends examplemodule.ExampleModule

  }

}
