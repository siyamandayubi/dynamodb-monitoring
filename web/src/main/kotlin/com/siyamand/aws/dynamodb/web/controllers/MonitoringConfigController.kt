package com.siyamand.aws.dynamodb.web.controllers

import com.siyamand.aws.dynamodb.core.common.PageResultEntity
import com.siyamand.aws.dynamodb.core.monitoring.MetadataService
import com.siyamand.aws.dynamodb.core.monitoring.WorkflowService
import com.siyamand.aws.dynamodb.core.monitoring.PrerequisiteService
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.AggregateMonitoringEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.MonitoringBaseEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.PrerequisteEntity
import com.siyamand.aws.dynamodb.core.sdk.authentication.BasicCredentialEntity
import com.siyamand.aws.dynamodb.web.models.CredentialModel
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class MonitoringConfigController(
        private val workflowService: WorkflowService,
        private val metadataService: MetadataService,
        private val prerequisiteService: PrerequisiteService) {

    @GetMapping("/api/monitoring/items")
    suspend fun getMonitoringStatus(@RequestParam(required = false) startKey :String = ""): HttpEntity<PageResultEntity<MonitoringBaseEntity<AggregateMonitoringEntity>>> {
        val result = metadataService.getMonitoredTables(startKey)
        return ResponseEntity(result, HttpStatus.OK)
    }

    @GetMapping("/api/monitoring/prerequisite")
    suspend fun getPrerequisite(): HttpEntity<PrerequisteEntity> {
        val result = prerequisiteService.getPrerequistes()
        return ResponseEntity(result, HttpStatus.OK)
    }

    @PostMapping("/api/monitoring/create-prerequisites")
    suspend fun createPrerequisites(@RequestBody credentialModel: CredentialModel): HttpEntity<PrerequisteEntity> {
        val result = prerequisiteService.createPrerequistes(BasicCredentialEntity(credentialModel.keyId, credentialModel.secretKeyId, null))
        return ResponseEntity(result, HttpStatus.OK)
    }
}