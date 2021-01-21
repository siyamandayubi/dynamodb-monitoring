package com.siyamand.aws.dynamodb.infrastructure.services

import com.siyamand.aws.dynamodb.core.services.MonitorConfigProvider

class StaticMonitorConfigProviderImpl : MonitorConfigProvider {
    // make it configurable
    private val version = "1"
    override fun getMonitoringConfigMetadataTable(): String {
        return "_MonigoringMetadata"
    }

    override fun getProxyTagName(): String {
        return "DynamoDbMonitoringProxy"
    }

    override fun getMonitoringVersionTagName(): String {
        return "Monitoring_Version"
    }

    override fun getMonitoringVersionValue(): String {
        return version
    }

    override fun getFunctionTagName(): String {
        return "Monitoring_Lambda_Name"
    }

    override fun getMonitoringGeneralTagName(): String {
        return "Monitoring-Lambda-Role"
    }

    override fun getFunctionTagValue(): String {
        TODO("Not yet implemented")
    }

    override fun getDatabaseTagName(): String {
        return "DynamoDbMonitoringDatabase_Name"
    }

    override fun getAccessTagName(): String {
        return "DynamoDbMonitoringAccess_Name"
    }

    override fun getS3BucketDefaultName(): String {
        return "dynamodbmonitoringbucket"
    }
}