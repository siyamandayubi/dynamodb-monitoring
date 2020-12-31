package com.siyamand.aws.dynamodb.core.services

interface MonitorConfigProvider {
    fun getMonitoringConfigMetadataTable(): String
    fun getDynamodbTagName(): String
    fun getDynamodbTagValue(): String
    fun getFunctionTagName(): String
    fun getFunctionTagValue(): String
    fun getDatabaseTagName(): String
    fun getDatabaseTagValue(): String
}