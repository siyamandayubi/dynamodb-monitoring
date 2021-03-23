package com.siyamand.aws.dynamodb.core.common

interface MonitorConfigProvider {
    fun getMonitoringConfigMetadataTable(): String
    fun getMonitoringVersionTagName(): String
    fun getMonitoringVersionValue(): String
    fun getMonitoringMetadataIdTagName(): String
    fun getAccessTagName(): String
    fun getS3BucketDefaultName(): String
    fun getLambdaRole(): String
    fun getRdsProxyRole(): String
    fun getAppConfigRole(): String
    fun getAppConfigLayerArn(region: String): String
    fun getMonitoringTableSourceTableIndexName(): String
}