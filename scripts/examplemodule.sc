import mill._
import mill.scalalib._
import mill.scalajslib._
import mill.scalajslib.api._

import $ivy.`org.typelevel::scalac-options:0.1.7`, org.typelevel.scalacoptions._

trait ExampleModule extends ScalaJSModule {
  def scalaVersion   = "3.6.4"
  def scalaJSVersion = "1.19.0"

  override def scalacOptions = T {
    val flags = super.scalacOptions() ++
      ScalacOptions.defaultTokensForVersion(ScalaVersion.unsafeFromString(scalaVersion())) ++
      Seq("-Xfatal-warnings")

    /*
    By default, we get the following flags:

    -encoding, utf8, -deprecation, -feature, -unchecked,
    -language:experimental.macros, -language:higherKinds,
    -language:implicitConversions, -Xkind-projector,
    -Wvalue-discard, -Wnonunit-statement, -Wunused:implicits,
    -Wunused:explicits, -Wunused:imports, -Wunused:locals,
    -Wunused:params, -Wunused:privates, -Xfatal-warnings
     */

    // Alledgedly unused local definitions are unavoidable in Ultraviolet,
    // so we remove the flag to make things tolerable.
    flags.filterNot(_ == "-Wunused:locals")
  }

  val tyrianVersion = "0.14.0"

  def ivyDeps =
    Agg(
      ivy"io.indigoengine::tyrian-io::$tyrianVersion"
    )

  override def moduleKind = T(mill.scalajslib.api.ModuleKind.ESModule)

  object test extends ScalaJSTests {
    def ivyDeps = Agg(
      ivy"org.scalameta::munit::1.1.1"
    )

    override def moduleKind = T(mill.scalajslib.api.ModuleKind.CommonJSModule)

    def testFramework: mill.T[String] = T("munit.Framework")
  }

}
