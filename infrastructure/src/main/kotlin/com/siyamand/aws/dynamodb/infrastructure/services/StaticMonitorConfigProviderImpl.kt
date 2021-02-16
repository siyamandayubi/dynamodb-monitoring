package com.siyamand.aws.dynamodb.infrastructure.services

import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider

class StaticMonitorConfigProviderImpl : MonitorConfigProvider {
    // make it configurable
    private val version = "1"
    override fun getMonitoringConfigMetadataTable(): String {
        return "_MonigoringMetadata"
    }

    override fun getMonitoringVersionTagName(): String {
        return "Monitoring_Version"
    }

    override fun getMonitoringVersionValue(): String {
        return version
    }

    override fun getMonitoringMetadataIdTagName(): String {
        return "Monitoring-Metadata-Id"
    }

    override fun getAccessTagName(): String {
        return "DynamoDbMonitoringAccess_Name"
    }

    override fun getS3BucketDefaultName(): String {
        return "dynamodbmonitoringbucket"
    }
}