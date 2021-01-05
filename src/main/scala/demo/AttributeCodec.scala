package demo

import software.amazon.awssdk.services.dynamodb.model.AttributeValue

trait AttributeCodec[A] {
  def encode(a: A): AttributeValue
  def decode(a: AttributeValue): A
}

object AttributeCodec {
  def apply[A](using codec: AttributeCodec[A]): AttributeCodec[A] = codec
}

given AttributeCodec[String] with {
  def encode(a: String): AttributeValue = AttributeValue.builder().s(a).build()
  def decode(a: AttributeValue): String = a.s()
}

implicit val intCodec: AttributeCodec[Int] = new AttributeCodec[Int] {
  def encode(a: Int): AttributeValue = AttributeValue.builder().n(a.toString).build()
  def decode(a: AttributeValue): Int = a.n().toInt
}
