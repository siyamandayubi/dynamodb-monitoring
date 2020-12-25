package com.siyamand.aws.dynamodb.web.controllers

import com.siyamand.aws.dynamodb.core.services.MetadataService
import com.siyamand.aws.dynamodb.web.models.MonitoringConfigModel
import org.springframework.http.HttpEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class MonitoringConfigController(private val metadataService: MetadataService) {

    @GetMapping("/api/monitoring-config/status")
    suspend fun getMonitoringStatus(): HttpEntity<MonitoringConfigModel> {
        val metaDataEntity = metadataService.load()
        return if (metaDataEntity == null) {
            HttpEntity(MonitoringConfigModel("NOT_CREATED"))
        } else {
            HttpEntity(MonitoringConfigModel("CREATED"))
        }
    }

    @PostMapping("/api/monitoring-config/create")
    suspend fun create(): HttpEntity<MonitoringConfigModel> {
        var metaDataEntity = metadataService.load()
        if (metaDataEntity == null) {
            metadataService.create()
        }
        return HttpEntity(MonitoringConfigModel("CREATED"))
    }
}