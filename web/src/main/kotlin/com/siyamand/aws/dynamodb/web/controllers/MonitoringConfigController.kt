package com.siyamand.aws.dynamodb.web.controllers

import com.siyamand.aws.dynamodb.core.common.PageResultEntity
import com.siyamand.aws.dynamodb.core.monitoring.MetadataService
import com.siyamand.aws.dynamodb.core.monitoring.PrerequisiteService
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.AggregateMonitoringEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.MonitoringBaseEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.PrerequisteEntity
import com.siyamand.aws.dynamodb.core.sdk.authentication.BasicCredentialEntity
import com.siyamand.aws.dynamodb.web.models.CredentialModel
import com.siyamand.aws.dynamodb.web.models.MonitoringDetailModel
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class MonitoringConfigController(
        private val metadataService: MetadataService,
        private val prerequisiteService: PrerequisiteService) {

    @GetMapping("/api/monitoring/items")
    suspend fun getMonitoringRecords(@RequestParam(required = false) startKey: String = ""): HttpEntity<PageResultEntity<MonitoringBaseEntity<AggregateMonitoringEntity>>> {
        val result = metadataService.getMonitoringRecords(startKey)
        return ResponseEntity(result, HttpStatus.OK)
    }

    @GetMapping("/api/monitoring/items/{id}")
    suspend fun getMonitoringRecord(@PathVariable id: String = ""): HttpEntity<MonitoringDetailModel> {
        val detail = metadataService.getMonitoringRecord(id) ?: throw Exception("no record found with id=$id")
        val resources = metadataService.getMonitoringItemResources(id)
        return ResponseEntity(MonitoringDetailModel(detail, resources), HttpStatus.OK)
    }

    @GetMapping("/api/monitoring/prerequisite")
    suspend fun getPrerequisite(): HttpEntity<PrerequisteEntity> {
        val result = prerequisiteService.getPrerequistes()
        return ResponseEntity(result, HttpStatus.OK)
    }

    @PostMapping("/api/monitoring/create-prerequisites")
    suspend fun createPrerequisites(@RequestBody credentialModel: CredentialModel): HttpEntity<PrerequisteEntity> {
        val result = prerequisiteService.createPrerequisites(BasicCredentialEntity(credentialModel.keyId, credentialModel.secretKeyId, null))
        return ResponseEntity(result, HttpStatus.OK)
    }
}