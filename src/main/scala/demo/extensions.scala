package demo

import software.amazon.awssdk.services.dynamodb.model.{GetItemRequest, PutItemRequest}

import scala.jdk.CollectionConverters._

extension[T] (b: GetItemRequest.Builder) {
  inline def key: GetItemRequestBuilderExtension[T] =
    new GetItemRequestBuilderExtension[T](b)
}

class GetItemRequestBuilderExtension[T](b: GetItemRequest.Builder) {
  inline def apply[A: AttributeCodec](inline k: T => A, v: A): GetItemRequest.Builder =
    b.key(Map(
      FieldName[T](k) -> AttributeCodec[A].encode(v)
    ).asJava)
}

extension[T: ItemCodec] (b: PutItemRequest.Builder) {
  def item(t: T): PutItemRequest.Builder =
    b.item(ItemCodec[T].encode(t).asJava)
}
