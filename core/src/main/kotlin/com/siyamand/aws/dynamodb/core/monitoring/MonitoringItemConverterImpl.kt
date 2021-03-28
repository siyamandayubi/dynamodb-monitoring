package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.common.asEnumOrDefault
import com.siyamand.aws.dynamodb.core.sdk.dynamodb.AttributeValueEntity
import com.siyamand.aws.dynamodb.core.sdk.dynamodb.TableItemEntity
import com.siyamand.aws.dynamodb.core.sdk.dynamodb.TableItemReaderDecorator
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.*
import com.siyamand.aws.dynamodb.core.sdk.resource.ResourceRepository
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MonitoringItemConverterImpl(private val resourceRepository: ResourceRepository) : MonitoringItemConverter {
    override fun convertToAggregateEntity(tableItemEntity: TableItemEntity): MonitoringBaseEntity<AggregateMonitoringEntity> {

        val reader = TableItemReaderDecorator(tableItemEntity.attributes)
        val relatedDataStr = reader.str("relatedData")
        val aggregateMonitoringEntity = if (relatedDataStr.isNullOrEmpty()) AggregateMonitoringEntity() else Json.decodeFromString(relatedDataStr)

        return MonitoringBaseEntity(
                reader.str("id"),
                reader.str("sourceTable"),
                reader.str("type"),
                reader.str("status").asEnumOrDefault(MonitorStatus.PENDING),
                reader.int("version") ?: 0,
                reader.str("workflowS3Arn"),
                aggregateMonitoringEntity
        )
    }

    override fun convertToMonitoringResourceEntity(tableItemEntity: TableItemEntity): MonitoringResourceEntity {
        if(!tableItemEntity.attributes.containsKey("id")){
            throw Exception("id column doesn't exist")
        }

        if(!tableItemEntity.attributes.containsKey("arn")){
            throw Exception("arn column doesn't exist")
        }

        return MonitoringResourceEntity(
                tableItemEntity.attributes["id"]?.stringValue ?: "",
                resourceRepository.convert(tableItemEntity.attributes["arn"]?.stringValue!!))
    }

    override fun convert(tableName: String, entity: MonitoringBaseEntity<AggregateMonitoringEntity>): TableItemEntity {
        val relatedData = Json.encodeToString(entity.relatedData)
        val attributes = mapOf(
                "id" to AttributeValueEntity(entity.id),
                "sourceTable" to AttributeValueEntity(entity.sourceTable),
                "type" to AttributeValueEntity(entity.type),
                "status" to AttributeValueEntity(entity.status.name),
                "version" to AttributeValueEntity(entity.version),
                "workflowS3Arn" to AttributeValueEntity(entity.workflowS3Key),
                "relatedData" to AttributeValueEntity(relatedData)
        )

        val key = mapOf("id" to AttributeValueEntity(entity.id))

        val returnValue = TableItemEntity(tableName)
        returnValue.attributes.putAll(attributes)
        returnValue.key.putAll(key)
        return returnValue
    }
}