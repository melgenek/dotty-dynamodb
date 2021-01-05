package demo

import software.amazon.awssdk.services.dynamodb.model.AttributeValue

import scala.compiletime._
import scala.deriving._
import scala.quoted.Expr

trait ItemCodec[T] {
  def encode(t: T): Map[String, AttributeValue]
  def decode(map: Map[String, AttributeValue]): T
}

object ItemCodec {

  def apply[A: ItemCodec]: ItemCodec[A] = summon[ItemCodec[A]]

  private inline def getAttributeNamesAndCodecs[N <: Tuple, T <: Tuple]: List[(String, AttributeCodec[Any])] =
    inline (erasedValue[N], erasedValue[T]) match {
      case (_: EmptyTuple, _: EmptyTuple) => Nil
      case (_: (nameHead *: nameTail), _: (typeHead *: typeTail)) =>
        val attributeLabel = constValue[nameHead].toString
        val attributeCodec = summonInline[AttributeCodec[typeHead]].asInstanceOf[AttributeCodec[Any]]
        (attributeLabel, attributeCodec) :: getAttributeNamesAndCodecs[nameTail, typeTail]
    }

  inline given derived[T <: Product](using m: Mirror.ProductOf[T]): ItemCodec[T] = {
    val namesAndCodecs = getAttributeNamesAndCodecs[m.MirroredElemLabels, m.MirroredElemTypes]
    new ItemCodec[T] {
      override def encode(t: T): Map[String, AttributeValue] = {
        namesAndCodecs.zip(t.productIterator)
          .map { case ((name, codec), value) =>
            name -> codec.encode(value)
          }
          .toMap
      }
      override def decode(map: Map[String, AttributeValue]): T = {
        val values = namesAndCodecs.map((name, codec) => codec.decode(map(name)))
        m.fromProduct(Tuple.fromArray(values.toArray))
      }
    }
  }

}
