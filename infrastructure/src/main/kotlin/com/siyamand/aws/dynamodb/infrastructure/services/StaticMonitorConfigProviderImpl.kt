package com.siyamand.aws.dynamodb.infrastructure.services

import com.siyamand.aws.dynamodb.core.services.MonitorConfigProvider

class StaticMonitorConfigProviderImpl: MonitorConfigProvider {
    override fun getMonitoringConfigMetadataTable(): String {
        return "_MonigoringMetadata"
    }

    override fun getTableNamePreFix(): String {
        return "_Aggregate"
    }

    override fun getFunctionNamePrefix(): String {
        return "_AggregateFunc"
    }
}