package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.common.asEnumOrDefault
import com.siyamand.aws.dynamodb.core.dynamodb.AttributeValueEntity
import com.siyamand.aws.dynamodb.core.dynamodb.TableItemEntity
import com.siyamand.aws.dynamodb.core.dynamodb.TableItemReaderDecorator
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.*

class MonitoringItemConverterImpl : MonitoringItemConverter {
    override fun convertToAggregateEntity(tableItemEntity: TableItemEntity): MonitoringBaseEntity<AggregateMonitoringEntity> {

        val aggregateMonitoringEntity = AggregateMonitoringEntity()
        val reader = TableItemReaderDecorator(tableItemEntity.attributes)
        val relatedData = reader.complex("relatedData")
        aggregateMonitoringEntity.databaseName = reader.str("databaseName")
        aggregateMonitoringEntity.databaseInstanceArn = reader.str("databaseInstanceArn")

        aggregateMonitoringEntity.fields.addAll(reader.complexArray("fields").map {
            AggregateFieldEntity(
                    it.str("name"),
                    it.str("path"),
                    it.instant("from"),
                    it.str("tableName"),
                    AggregateType.valueOf(it.str("aggregateType")))
        })

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
        val relatedData = mapOf(
                "databaseInstanceArn" to AttributeValueEntity(entity.relatedData.databaseInstanceArn),
                "databaseName" to AttributeValueEntity(entity.relatedData.databaseName),
                "fields" to AttributeValueEntity(entity.relatedData.fields.map {
                    mapOf(
                            "from" to AttributeValueEntity(it.from),
                            "name" to AttributeValueEntity(it.name),
                            "tableName" to AttributeValueEntity(it.tableName),
                            "path" to AttributeValueEntity(it.path),
                    )
                }.toTypedArray())
        )
        val attributes = mapOf(
                "id" to AttributeValueEntity(entity.id),
                "sourceTable" to AttributeValueEntity(entity.sourceTable),
                "type" to AttributeValueEntity(entity.type),
                "status" to AttributeValueEntity(entity.status.name),
                "version" to AttributeValueEntity(entity.version),
                "workflow" to AttributeValueEntity(entity.workflow),
                "relatedData" to AttributeValueEntity(relatedData)
        )

        val returnValue = TableItemEntity(tableName)
        returnValue.attributes.putAll(attributes)

        return  returnValue
    }
}