package com.siyamand.aws.dynamodb.web.controllers

import com.siyamand.aws.dynamodb.core.monitoring.MetadataService
import com.siyamand.aws.dynamodb.core.monitoring.PrerequisiteService
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.AggregateMonitoringEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.MonitoringBaseEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.PrerequisteEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MonitoringConfigController(private val metadataService: MetadataService, private val prerequisiteService: PrerequisiteService) {

    @GetMapping("/api/monitoring/items")
    suspend fun getMonitoringStatus(): HttpEntity<List<MonitoringBaseEntity<AggregateMonitoringEntity>>> {
        val result = metadataService.getMonitoredTables()
        return ResponseEntity(result, HttpStatus.OK)
    }

    @GetMapping("/api/monitoring/prerequisite")
    suspend fun getPrerequisite(): HttpEntity<PrerequisteEntity> {
        val result = prerequisiteService.getPrerequistes()
        return ResponseEntity(result, HttpStatus.OK)
    }

}