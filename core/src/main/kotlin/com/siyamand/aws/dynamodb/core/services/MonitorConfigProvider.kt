package com.siyamand.aws.dynamodb.core.services

interface MonitorConfigProvider {
    fun getMonitoringConfigMetadataTable(): String
    fun getTableNamePreFix(): String
    fun getFunctionNamePrefix(): String
}