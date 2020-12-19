package com.siyamand.aws.dynamodb.core.services

import com.siyamand.aws.dynamodb.core.entities.monitoring.MonitorEntity

interface MonitorService {
    fun getMonitors(): List<MonitorEntity>
}