package com.siyamand.aws.dynamodb.core.services

interface MonitorConfigProvider {
    fun getMonitoringConfigTable(): String
    fun getTableNamePreFix(): String
    fun getFunctionNamePrefix(): String
}