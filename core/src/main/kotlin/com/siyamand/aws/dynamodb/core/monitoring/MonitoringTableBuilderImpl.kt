package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider
import com.siyamand.aws.dynamodb.core.sdk.dynamodb.*

class MonitoringTableBuilderImpl(val configProvider: MonitorConfigProvider) : MonitoringTableBuilder {
    override val keyName: String = "id"
    override fun build(tableName: String): TableDetailEntity {

        return TableDetailEntity(
                "",
                tableName,
                mutableListOf(
                        TableAttribute(keyName, "S")
                        //TableAttribute("sourceTable", "S"),
                        //TableAttribute("type", "S"),
                        //TableAttribute("status", "S"),
                        //TableAttribute("version", "N"),
                        //TableAttribute("workflow", "S"),
                ),
                mutableListOf(TableKeyScheme(keyName, "HASH", "S")),
                listOf(IndexEntity(configProvider.getMonitoringTableSourceTableIndexName(),"", listOf(TableKeyScheme("sourceTable", "HASH", "S")))),
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