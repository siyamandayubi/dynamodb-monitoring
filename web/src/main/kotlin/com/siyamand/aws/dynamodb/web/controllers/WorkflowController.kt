package com.siyamand.aws.dynamodb.web.controllers

import com.siyamand.aws.dynamodb.core.monitoring.WorkflowService
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.StartMonitoringWorkflowEntity
import com.siyamand.aws.dynamodb.core.schedule.WorkflowJobHandler
import com.siyamand.aws.dynamodb.web.models.ResumeWorkflowModel
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class WorkflowController(private val workflowJobHandler: WorkflowJobHandler, private val metadataService: WorkflowService) {
    @PostMapping("/api/workflow/start")
    suspend fun startWorkflow(@RequestBody model: StartMonitoringWorkflowEntity): HttpEntity<String> {

        if (model.definition == null){
            return ResponseEntity("definition is mandatory", HttpStatus.BAD_REQUEST)
        }
        metadataService.startWorkflow(model)
        return ResponseEntity("result", HttpStatus.OK)
    }


    @PostMapping("/api/Workflow/continue")
    suspend fun continueWorkflows(@RequestBody resumeWorkflowModel: ResumeWorkflowModel): HttpEntity<String> {
        val result = metadataService.resumeWorkflow(resumeWorkflowModel.id)
        return ResponseEntity("Finished", HttpStatus.OK)
    }
}