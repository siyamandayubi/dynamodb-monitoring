package com.siyamand.aws.dynamodb.infrastructure.mappers

import com.siyamand.aws.dynamodb.core.annotations.Async
import com.siyamand.aws.dynamodb.core.entities.TableAttribute
import com.siyamand.aws.dynamodb.core.entities.TableDetailEntity
import com.siyamand.aws.dynamodb.core.entities.TableKeyScheme
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement

@Async
class TableMapper {
    companion object {

        @Async
        fun convertDetail(describeTableResponse: DescribeTableResponse): TableDetailEntity? {
            if (describeTableResponse == null){
                return  null
            }

            val table = describeTableResponse.table()
            val attributes = table.attributeDefinitions().map { TableAttribute(it.attributeName(), it.attributeType().name) }
            val keySchema = table.keySchema().map { TableKeyScheme(it.attributeName(), it.keyType().name) }
            return TableDetailEntity(
                    table.tableArn(),
                    table.tableName(),
                    attributes.toMutableList(),
                    keySchema.toMutableList(),
                    table.tableStatusAsString())
        }

        fun convertToAttributeDefinition(attributeName:String, attributeType:String): AttributeDefinition{
            return AttributeDefinition.builder().attributeName(attributeName).attributeType(attributeType).build()
        }
        fun convertToKeySchemaDefinition(attributeName:String, keyType:String): KeySchemaElement{
            return KeySchemaElement.builder().attributeName(attributeName).keyType(keyType).build()
        }
    }
}