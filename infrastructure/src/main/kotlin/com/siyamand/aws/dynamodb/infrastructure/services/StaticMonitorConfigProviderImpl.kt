package com.siyamand.aws.dynamodb.infrastructure.services

import com.siyamand.aws.dynamodb.core.services.MonitorConfigProvider

class StaticMonitorConfigProviderImpl: MonitorConfigProvider {
    // make it configurable
    private val version = "1"
    override fun getMonitoringConfigMetadataTable(): String {
        return "_MonigoringMetadata"
    }

    override fun getDynamodbTagName(): String {
        return "_Aggregate"
    }

    override fun getDynamodbTagValue(): String {
        TODO("Not yet implemented")
    }

    override fun getFunctionTagName(): String {
        return "_AggregateFunc"
    }

    override fun getFunctionTagValue(): String {
        TODO("Not yet implemented")
    }

    override fun getDatabaseTagName(): String {
        TODO("Not yet implemented")
    }

    override fun getDatabaseTagValue(): String {
        TODO("Not yet implemented")
    }
}