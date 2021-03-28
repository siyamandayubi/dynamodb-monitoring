package com.siyamand.aws.dynamodb.infrastructure.services

import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider

class StaticMonitorConfigProviderImpl : MonitorConfigProvider {
    // make it configurable
    private val version = "1"

    override fun getMonitoringResourcesTableName(): String {
        return "_MonitoringResources"
    }

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

    override fun getLambdaRole(): String {
        return "Dynamodb-Monitoring-DB-Role"
    }

    override fun getAppConfigRole(): String {
        return "Dynamodb-Monitoring-AppConfig-Role"
    }

    override fun getRdsProxyRole(): String {
        return "Rds-Proxy-Monitoring-Role"
    }

    override fun getMonitoringTableSourceTableIndexName(): String {
        return "sourceTableIndex";
    }

    override fun getMonitoringResourceTableIndexName(): String {
        return "arnIndex";
    }

    override fun getAppConfigLayerArn(region: String): String {
        return "arn:aws:lambda:$region:728743619870:layer:AWS-AppConfig-Extension:15"
    }
}