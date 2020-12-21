package com.siyamand.aws.dynamodb.core.builders

import com.siyamand.aws.dynamodb.core.entities.TableAttribute
import com.siyamand.aws.dynamodb.core.entities.TableDetailEntity
import com.siyamand.aws.dynamodb.core.entities.TableKeyScheme
import com.siyamand.aws.dynamodb.core.services.MonitorConfigProvider

class MetadataTableBuilderImpl : MetadataTableBuilder {
    override fun build(monitorConfigProvider: MonitorConfigProvider): TableDetailEntity {
        val tableName = monitorConfigProvider.getMonitoringConfigMetadataTable()
        return TableDetailEntity(
                "",
                tableName,
                mutableListOf(TableAttribute("Config", "S")),
                mutableListOf(TableKeyScheme("TableName", "HASH")),
                "ACTIVE")
    }
}