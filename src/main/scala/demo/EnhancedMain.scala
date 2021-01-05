package demo

import demo.Main.item
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model._

import java.net.URI
import scala.jdk.CollectionConverters._

object EnhancedMain extends App {
  val ddb = DynamoDbClient
    .builder()
    .endpointOverride(new URI("http://localhost:8000"))
    .build()

  ddb.createTable(
    CreateTableRequest.builder()
      .tableName(TableName)
      .keySchema(
        KeySchemaElement.builder()
          .attributeName(FieldName[NewYear](_.year))
          .keyType(KeyType.HASH)
          .build())
      .attributeDefinitions(
        AttributeDefinition.builder()
          .attributeName(FieldName[NewYear](_.year))
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
      .item(year)
      .build()
  )

  val item = ddb.getItem(
    GetItemRequest.builder()
      .tableName(TableName)
      .key[NewYear](_.year, 2020)
      .build()
  )
  
  println(ItemCodec[NewYear].decode(item.item().asScala.toMap))

  ddb.deleteTable(DeleteTableRequest.builder().tableName(TableName).build())

}
