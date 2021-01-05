package demo.inline

import scala.quoted._

object InlineFunctions {
  inline def showExpr(expr: Any): String = ${showExprImpl('expr)}
  
  inline def showExprInlined(inline expr: Any): String = ${showExprImpl('expr)}
  
  private def showExprImpl(expr: Expr[Any])(using Quotes): Expr[String] =
    '{ ${Expr(expr.show)} + " = " + $expr }
}