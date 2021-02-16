package com.siyamand.aws.dynamodb.web.controllers

import com.siyamand.aws.dynamodb.core.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.monitoring.MetadataService
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.AggregateMonitoringEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.MonitoringBaseEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MonitoringConfigController(private val metadataService: MetadataService) {

    @GetMapping("/api/monitoring/items")
    suspend fun getMonitoringStatus(): HttpEntity<List<MonitoringBaseEntity<AggregateMonitoringEntity>>> {
        val result = metadataService.getMonitoredTables()
        return ResponseEntity(result, HttpStatus.OK)
    }

}