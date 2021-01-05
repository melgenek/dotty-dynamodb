package demo

import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model._

import java.net.URI
import scala.jdk.CollectionConverters._

object Main extends App {
  val ddb = DynamoDbClient
    .builder()
    .endpointOverride(new URI("http://localhost:8000"))
    .build()

  ddb.createTable(
    CreateTableRequest.builder()
      .tableName(TableName)
      .keySchema(
        KeySchemaElement.builder()
          .attributeName("year")
          .keyType(KeyType.HASH)
          .build())
      .attributeDefinitions(
        AttributeDefinition.builder()
          .attributeName("year")
          .attributeType(ScalarAttributeType.N)
          .build()
      )
      .provisionedThroughput(
        ProvisionedThroughput.builder()
          .readCapacityUnits(1)
          .writeCapacityUnits(1)
          .build()
      )
      .build()
  )

  ddb.putItem(
    PutItemRequest.builder()
      .tableName(TableName)
      .item(Map(
        "year" -> AttributeValue.builder().n(year.year.toString).build(),
        "wish" -> AttributeValue.builder().s(year.wish).build()
      ).asJava)
      .build()
  )

  val item = ddb.getItem(
    GetItemRequest.builder()
      .tableName(TableName)
      .key(Map(
        "year" -> AttributeValue.builder().n("2021").build()
      ).asJava)
      .build()
  )
  println(item)

  ddb.deleteTable(DeleteTableRequest.builder().tableName(TableName).build())

}
