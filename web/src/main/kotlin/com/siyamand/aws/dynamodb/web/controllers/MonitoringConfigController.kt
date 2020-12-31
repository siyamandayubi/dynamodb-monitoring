package com.siyamand.aws.dynamodb.web.controllers

import com.siyamand.aws.dynamodb.core.entities.ResourceEntity
import com.siyamand.aws.dynamodb.core.services.MetadataService
import com.siyamand.aws.dynamodb.web.models.MonitoringConfigModel
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class MonitoringConfigController(private val metadataService: MetadataService) {

    @GetMapping("/api/monitoring/tables")
     fun getMonitoringStatus(): HttpEntity<List<ResourceEntity>> {
        val result = metadataService.getMonitoredTables()
        return ResponseEntity(result, HttpStatus.OK)
    }

}