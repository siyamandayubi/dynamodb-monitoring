package com.siyamand.aws.dynamodb.web.controllers

import com.siyamand.aws.dynamodb.core.monitoring.MetadataService
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.AggregateMonitoringEntity
import com.siyamand.aws.dynamodb.core.schedule.WorkflowJobHandler
import com.siyamand.aws.dynamodb.core.workflow.WorkflowResult
import com.siyamand.aws.dynamodb.web.models.StartWorkflowModel
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class WorkflowController(private val workflowJobHandler: WorkflowJobHandler, private val metadataService: MetadataService) {
    @PostMapping("/api/workflow/start")
    suspend fun startWorkflow(@RequestBody startWorkflowModel: StartWorkflowModel): HttpEntity<String> {
        metadataService.startWorkflow(startWorkflowModel.sourceTableName, startWorkflowModel.workflowName, AggregateMonitoringEntity())
        return ResponseEntity("result", HttpStatus.OK)
    }

    @PostMapping("/api/Workflow/continue")
    suspend fun continueWorkflows(): HttpEntity<String> {
        val result = workflowJobHandler.execute()
        return ResponseEntity("Finished", HttpStatus.OK)
    }
}