package com.siyamand.aws.dynamodb.core.common

interface MonitorConfigProvider {
    fun getMonitoringConfigMetadataTable(): String
    fun getMonitoringVersionTagName(): String
    fun getMonitoringVersionValue(): String
    fun getFunctionTagName(): String
    fun getMonitoringGeneralTagName(): String
    fun getFunctionTagValue(): String
    fun getDatabaseTagName(): String
    fun getProxyTagName(): String
    fun getAccessTagName(): String
    fun getS3BucketDefaultName(): String
}