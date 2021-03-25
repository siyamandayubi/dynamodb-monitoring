package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider
import com.siyamand.aws.dynamodb.core.sdk.dynamodb.*

class MonitoringTableBuilderImpl(val configProvider: MonitorConfigProvider) : MonitoringTableBuilder {
    override val keyName: String = "id"
    override val resouceColumnName: String = "arn"
    override fun buildMonitoringMetadataTable(): TableDetailEntity {

        val tableName = configProvider.getMonitoringConfigMetadataTable()
        if (tableName.isNullOrEmpty()) {
            throw Exception("No config name for Monitoring Dynamodb table")
        }

        return TableDetailEntity(
                "",
                tableName,
                mutableListOf(
                        TableAttribute(keyName, "S")
                ),
                mutableListOf(TableKeyScheme(keyName, "HASH", "S")),
                listOf(IndexEntity(configProvider.getMonitoringTableSourceTableIndexName(), "", listOf(TableKeyScheme("sourceTable", "HASH", "S")))),
                "",
                false,
                null)
    }

    override fun createResourceItem(monitoringId: String, arn: String): TableItemEntity {
        val item = TableItemEntity(configProvider.getMonitoringResourcesTableName())
        item.attributes[keyName] = AttributeValueEntity(monitoringId)
        item.attributes[resouceColumnName] = AttributeValueEntity(arn)
        item.key[keyName] = AttributeValueEntity(monitoringId)
        item.key[resouceColumnName] = AttributeValueEntity(arn)
        return item
    }

    override fun buildMonitoringResourceTable(): TableDetailEntity {
        val tableName = configProvider.getMonitoringResourcesTableName()
        if (tableName.isNullOrEmpty()) {
            throw Exception("No config name for Monitoring Dynamodb table")
        }

        return TableDetailEntity(
                "",
                tableName,
                mutableListOf(
                        TableAttribute(keyName, "S"),
                        TableAttribute(resouceColumnName, "S")
                ),
                mutableListOf(TableKeyScheme(keyName, "HASH", "S"),
                        TableKeyScheme(resouceColumnName, "RANGE", "S")),
                listOf(IndexEntity("${resouceColumnName}Index", "", listOf(TableKeyScheme(resouceColumnName, "HASH", "S")))),
                "",
                false,
                null)
    }

    override fun buildIndexRequest(): CreateIndexEntity {
        return CreateIndexEntity(
                configProvider.getMonitoringTableSourceTableIndexName(),
                listOf(TableKeyScheme("sourceTable", "HASH", "S")))
    }
}