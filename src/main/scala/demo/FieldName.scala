package demo

import scala.quoted.Expr.{ofTuple, summon}
import scala.quoted._

object FieldName {
  inline def apply[T](inline f: T => Any): String = ${getName('f)}

  private def getName[T](f: Expr[T => Any])(using Type[T], Quotes): Expr[String] = {
    import quotes.reflect._
    val acc = new TreeAccumulator[String] {
      def foldTree(names: String, tree: Tree)(owner: Symbol): String = tree match {
        case Select(_, name) => name
        case _ => foldOverTree(names, tree)(owner)
      }
    }
    val fieldName = acc.foldTree(null, f.asTerm)(Symbol.spliceOwner)
    val primaryConstructorFields = TypeTree.of[T].symbol.caseFields.map(_.name)
    if(!primaryConstructorFields.contains(fieldName))
      report.error(s"The field '$fieldName' is not one of the primary constructor parameter.", f)
    Expr(fieldName)
  }
}
