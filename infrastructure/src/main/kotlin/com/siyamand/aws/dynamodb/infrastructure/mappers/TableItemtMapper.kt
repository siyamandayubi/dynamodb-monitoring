package com.siyamand.aws.dynamodb.infrastructure.mappers

import com.siyamand.aws.dynamodb.core.sdk.dynamodb.AttributeValueEntity
import com.siyamand.aws.dynamodb.core.sdk.dynamodb.AttributeValueType
import com.siyamand.aws.dynamodb.core.sdk.dynamodb.TableItemEntity
import software.amazon.awssdk.services.dynamodb.model.*

class TableItemtMapper {
    companion object {
        fun convertRequest(entity: TableItemEntity): PutItemRequest {
            val builder = PutItemRequest
                    .builder()
                    .item(entity.attributes.mapValues { convertAttributeValue(it.value) })
                    .tableName(entity.tableName)

            return builder.build()
        }

        fun convertUpdateRequest(entity: TableItemEntity): UpdateItemRequest {
            val builder = UpdateItemRequest
                    .builder()
                    .attributeUpdates(entity.attributes.filter { entity.key.any { k-> k.key != it.key } }.mapValues { convertToUpdateAttributeValue(it.value) })
                    .key(entity.key.mapValues { convertAttributeValue(it.value) })
                    .tableName(entity.tableName)

            return builder.build()
        }

        fun convertResponse(tableName:String, response: PutItemResponse): TableItemEntity {
            val entity = TableItemEntity(tableName)
            entity.attributes.putAll(response.attributes().mapValues { convertToAttributeValueEntity(it.value) })
            return entity
        }

        fun convertKey(key: Map<String, AttributeValueEntity>): Map<String, AttributeValue> {
            return key.mapValues { convertAttributeValue(it.value) }
        }

        fun convertAttributeValue(value: AttributeValueEntity): AttributeValue {
            val builder = AttributeValue.builder()
            when (value.type) {
                AttributeValueType.STRING -> builder.s(value.stringValue)
                AttributeValueType.INT -> builder.n(value.intValue.toString())
                AttributeValueType.BOOL -> builder.bool(value.boolValue)
                AttributeValueType.STRING_ARRAY -> builder.ss(value.stringArrayValue?.toList())
                AttributeValueType.INT_ARRAY -> builder.ns(value.intArrayValue?.map { it.toString() })
                AttributeValueType.COMPLEX -> builder.m(value.complexValue?.mapValues { convertAttributeValue(it.value) })
                else -> builder.s(value.toString())
            }

            return builder.build()
        }

        fun convertToUpdateAttributeValue(value: AttributeValueEntity): AttributeValueUpdate {
            val builder = AttributeValueUpdate.builder().value(convertAttributeValue(value))

            return builder.build()
        }

        fun convertToAttributeValueEntity(value: AttributeValue): AttributeValueEntity {
            return when {
                value.s() != null -> {
                    AttributeValueEntity(value.s())
                }
                value.hasSs() -> {
                    AttributeValueEntity(value.ss().toTypedArray())
                }
                value.n() != null -> {
                    AttributeValueEntity(value.n().toInt())
                }
                value.hasM() -> {
                    AttributeValueEntity(value.m().mapValues { convertToAttributeValueEntity(it.value) })
                }
                value.hasNs() -> {
                    AttributeValueEntity(value.ns().map { it.toInt() }.toTypedArray())
                }
                else -> AttributeValueEntity()
            }
        }
    }
}