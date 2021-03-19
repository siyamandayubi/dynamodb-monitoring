package com.siyamand.aws.dynamodb.web.controllers

import com.siyamand.aws.dynamodb.core.monitoring.MetadataService
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.AggregateFieldEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.AggregateMonitoringEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.GroupByEntity
import com.siyamand.aws.dynamodb.core.schedule.WorkflowJobHandler
import com.siyamand.aws.dynamodb.core.workflow.WorkflowResult
import com.siyamand.aws.dynamodb.web.models.ResumeWorkflowModel
import com.siyamand.aws.dynamodb.web.models.StartWorkflowModel
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
class WorkflowController(private val workflowJobHandler: WorkflowJobHandler, private val metadataService: MetadataService) {
    @PostMapping("/api/workflow/start")
    suspend fun startWorkflow(@RequestBody startWorkflowModel: StartWorkflowModel): HttpEntity<String> {
        val aggregateMonitoringEntity = AggregateMonitoringEntity()
        aggregateMonitoringEntity.databaseName = startWorkflowModel.databaseName
        aggregateMonitoringEntity.instancesCount = 2
        aggregateMonitoringEntity.groups.add({
            val groupByEntity = GroupByEntity()
            groupByEntity.fieldName = "category"
            groupByEntity.tableName = "categoryAggr"
            groupByEntity.path = "category"
            groupByEntity.fields.add(AggregateFieldEntity("name", "name", Instant.now()))
            groupByEntity
        }())
        metadataService.startWorkflow(startWorkflowModel.sourceTableName, startWorkflowModel.workflowName, aggregateMonitoringEntity)
        return ResponseEntity("result", HttpStatus.OK)
    }


    @PostMapping("/api/Workflow/continue")
    suspend fun continueWorkflows(@RequestBody resumeWorkflowModel: ResumeWorkflowModel): HttpEntity<String> {
        val result = metadataService.resumeWorkflow(resumeWorkflowModel.id)
        return ResponseEntity("Finished", HttpStatus.OK)
    }
}