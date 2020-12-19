package com.siyamand.aws.dynamodb.infrastructure.mappers

import com.siyamand.aws.dynamodb.core.entities.TableAttribute
import com.siyamand.aws.dynamodb.core.entities.TableDetailEntity
import com.siyamand.aws.dynamodb.core.entities.TableKeyScheme
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse

class TableMapper {
    companion object {

        fun convertDetail(describeTableResponse: DescribeTableResponse): TableDetailEntity {
            if (describeTableResponse == null){
                throw IllegalArgumentException("description is null")
            }

            val table = describeTableResponse.table()
            val attributes = table.attributeDefinitions().map { TableAttribute(it.attributeName(), it.attributeType().name) }
            val keySchema = table.keySchema().map { TableKeyScheme(it.attributeName(), it.keyType().name) }
            return TableDetailEntity(attributes.toMutableList(), keySchema.toMutableList())
        }
    }
}