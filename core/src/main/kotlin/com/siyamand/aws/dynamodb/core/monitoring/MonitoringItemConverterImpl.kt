package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.common.asEnumOrDefault
import com.siyamand.aws.dynamodb.core.dynamodb.AttributeValueEntity
import com.siyamand.aws.dynamodb.core.dynamodb.TableItemEntity
import com.siyamand.aws.dynamodb.core.dynamodb.TableItemReaderDecorator
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MonitoringItemConverterImpl : MonitoringItemConverter {
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
                reader.str("workflow"),
                aggregateMonitoringEntity
        )
    }


    override fun convert(tableName: String, entity: MonitoringBaseEntity<AggregateMonitoringEntity>): TableItemEntity {
        val relatedData = Json.encodeToString(entity.relatedData)
        val attributes = mapOf(
                "id" to AttributeValueEntity(entity.id),
                "sourceTable" to AttributeValueEntity(entity.sourceTable),
                "type" to AttributeValueEntity(entity.type),
                "status" to AttributeValueEntity(entity.status.name),
                "version" to AttributeValueEntity(entity.version),
                "workflow" to AttributeValueEntity(entity.workflow),
                "relatedData" to AttributeValueEntity(relatedData)
        )

        val key = mapOf("id" to AttributeValueEntity(entity.id))

        val returnValue = TableItemEntity(tableName)
        returnValue.attributes.putAll(attributes)
        returnValue.key.putAll(key)
        return returnValue
    }
}