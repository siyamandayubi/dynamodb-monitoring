package com.siyamand.aws.dynamodb.core.services

interface MonitorConfigProvider {
    fun getMonitoringConfigMetadataTable(): String
    fun getMonitoringVersionTagName(): String
    fun getMonitoringVersionValue(): String
    fun getFunctionTagName(): String
    fun getRoleTagName(): String
    fun getFunctionTagValue(): String
    fun getDatabaseTagName(): String
    fun getProxyTagName(): String
}