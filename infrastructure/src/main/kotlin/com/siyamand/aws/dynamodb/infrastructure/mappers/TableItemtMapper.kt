package com.siyamand.aws.dynamodb.infrastructure.mappers

import com.siyamand.aws.dynamodb.core.entities.item.AttributeValueEntity
import com.siyamand.aws.dynamodb.core.entities.item.AttributeValueType
import com.siyamand.aws.dynamodb.core.entities.item.TableItemEntity
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse

class TableItemtMapper {
    companion object {
        fun convertRequest(entity: TableItemEntity): PutItemRequest{
            val x = PutItemRequest.builder().item(mapOf())
            TODO()
        }

        fun convertResponse(response: PutItemResponse): TableItemEntity {
            TODO()
        }

        fun convertKey(key: Map<String, AttributeValueEntity>):Map<String, AttributeValue>{
            return key.mapValues { convertAttributeValue(it.value) }
        }

        fun convertAttributeValue(value: AttributeValueEntity): AttributeValue{
            val builder = AttributeValue.builder()
            when(value.type){
                AttributeValueType.STRING -> builder.s(value.stringValue)
                AttributeValueType.INT -> builder.n(value.intValue.toString())
                AttributeValueType.BOOL -> builder.bool(value.boolValue)
                AttributeValueType.STRING_ARRAY -> builder.ss(value.stringArrayValue?.toList())
                AttributeValueType.INT_ARRAY -> builder.ns(value.intArrayValue?.map { it.toString() })
                AttributeValueType.COMPLEX -> builder.m(value.complexValue?.mapValues { convertAttributeValue(it.value)  })
            }

            return  builder.build()
        }

        fun convertToAttributeValueEntity(value: AttributeValue): AttributeValueEntity{
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
                value.hasM() ->{
                    AttributeValueEntity(value.m().mapValues { convertToAttributeValueEntity(it.value) })
                }
                value.hasNs() ->{
                    AttributeValueEntity(value.ns().map { it.toInt() }.toTypedArray())
                }
                else -> AttributeValueEntity()
            }
        }
    }
}