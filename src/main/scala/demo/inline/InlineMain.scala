package demo.inline

import InlineFunctions._

object InlineMain extends App {
  val a = 1
  val b = 2

  println(showExprInlined(a + b))
  println(showExpr(a + b))
}
