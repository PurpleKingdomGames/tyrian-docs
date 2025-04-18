import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`
import mill._
import mill.scalalib._
import mill.scalajslib._
import mill.scalajslib.api._

object counter extends ScalaJSModule {

  def scalaVersion   = "3.6.3"
  def scalaJSVersion = "1.18.2"

  def buildSite() =
    T.command {
      T {
        compile()
        fastLinkJS()
      }
    }

  def ivyDeps =
    Agg(
      ivy"io.indigoengine::tyrian-io::${TyrianVersion.getVersion}"
    )

  override def moduleKind = T(mill.scalajslib.api.ModuleKind.CommonJSModule)

  object test extends ScalaJSTests {
    def ivyDeps = Agg(
      ivy"org.scalameta::munit::1.1.0"
    )

    def testFramework = "munit.Framework"

    override def moduleKind = T(mill.scalajslib.api.ModuleKind.CommonJSModule)

  }

}

object TyrianVersion {
  def getVersion: String = {
    def rec(path: String, levels: Int, version: Option[String]): String = {
      val msg = "ERROR: Couldn't find Tyrian version."
      version match {
        case Some(v) =>
          println(s"""Tyrian version set to '$v'""")
          v

        case None if levels < 3 =>
          try {
            val v = scala.io.Source.fromFile(path).getLines().toList.head
            rec(path, levels, Some(v))
          } catch {
            case _: Throwable =>
              rec("../" + path, levels + 1, None)
          }

        case None =>
          println(msg)
          throw new Exception(msg)
      }
    }

    rec(".tyrian-version", 0, None)
  }
}
