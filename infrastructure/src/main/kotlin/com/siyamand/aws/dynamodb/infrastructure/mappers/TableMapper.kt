package com.siyamand.aws.dynamodb.infrastructure.mappers

import com.siyamand.aws.dynamodb.core.annotations.Async
import com.siyamand.aws.dynamodb.core.sdk.dynamodb.*
import software.amazon.awssdk.services.dynamodb.model.*

@Async
class TableMapper {
    companion object {

        @Async
        fun convertDetail(describeTableResponse: DescribeTableResponse): TableDetailEntity? {
            if (describeTableResponse == null) {
                return null
            }

            val table = describeTableResponse.table()
            return convertDetail(table)
        }

        @Async
        fun convertDetail(table: TableDescription): TableDetailEntity {
            val attributes = table.attributeDefinitions().map { TableAttribute(it.attributeName(), it.attributeType().name) }
            val keySchema = table.keySchema().map { TableKeyScheme(it.attributeName(), it.keyType().name, "") }
            val streamArn = table.latestStreamArn()
            return TableDetailEntity(
                    table.tableArn(),
                    table.tableName(),
                    attributes.toMutableList(),
                    keySchema.toMutableList(),
                    table.globalSecondaryIndexes().map { IndexEntity(it.indexName(), it.indexStatusAsString(), it.keySchema().map { TableKeyScheme(it.attributeName(), it.keyType().name, "") }) },
                    table.tableStatusAsString(),
                    table.streamSpecification()?.streamEnabled() ?: false,
                    if (streamArn.isNullOrEmpty()) null else ResourceMapper.convert(streamArn!!)
            )
        }

        fun convertToAttributeDefinition(attributeName: String, attributeType: String): AttributeDefinition {
            return AttributeDefinition.builder().attributeName(attributeName).attributeType(attributeType).build()
        }

        fun convertToKeySchemaDefinition(attributeName: String, keyType: String): KeySchemaElement {
            return KeySchemaElement.builder().attributeName(attributeName).keyType(keyType).build()
        }

        fun convert(createIndexEntity: CreateIndexEntity): GlobalSecondaryIndexUpdate {
            return GlobalSecondaryIndexUpdate
                    .builder()
                    .create(CreateGlobalSecondaryIndexAction
                            .builder()
                            .indexName(createIndexEntity.indexName)
                            .projection(Projection.builder().projectionType(ProjectionType.ALL).build())
                            .keySchema(createIndexEntity.keySchema.map { convertToKeySchemaDefinition(it.attributeName, it.keyType) })
                            .build()
                    ).build()
        }

        fun convertIndex(createIndexEntity: IndexEntity): GlobalSecondaryIndex {
            return GlobalSecondaryIndex
                    .builder()
                    .indexName(createIndexEntity.indexName)
                    .projection(Projection.builder().projectionType(ProjectionType.ALL).build())
                    .keySchema(createIndexEntity.keySchema.map { convertToKeySchemaDefinition(it.attributeName, it.keyType) })
                    .build()
        }
    }
}