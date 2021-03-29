package com.siyamand.aws.dynamodb.core.schedule

import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider
import com.siyamand.aws.dynamodb.core.sdk.dynamodb.TableItemRepository
import com.siyamand.aws.dynamodb.core.monitoring.MonitoringItemConverter
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.MonitorStatus
import com.siyamand.aws.dynamodb.core.sdk.s3.S3Service
import com.siyamand.aws.dynamodb.core.workflow.WorkflowConverter
import com.siyamand.aws.dynamodb.core.workflow.WorkflowManager
import com.siyamand.aws.dynamodb.core.workflow.WorkflowPersister

class WorkflowJobHandlerImpl(private val monitorConfigProvider: MonitorConfigProvider,
                             private val s3Service: S3Service,
                             private val monitoringItemConverter: MonitoringItemConverter,
                             private val workflowConverter: WorkflowConverter,
                             private val workflowManager: WorkflowManager,
                             private val workflowPersister: WorkflowPersister,
                             private val tableItemRepository: TableItemRepository) : WorkflowJobHandler {
    override suspend fun execute() {
        val tableName = monitorConfigProvider.getMonitoringConfigMetadataTable()
        if (tableName.isEmpty()) {
            throw Exception("No config name for Monitoring Dynamodb table")
        }

        var list = tableItemRepository.getList(tableName, mapOf())
        while (list.items.any()) {
            val monitoringItems = list.items.map { monitoringItemConverter.convertToAggregateEntity(it) }
                    .filter { it.status == MonitorStatus.INITIAL || it.status == MonitorStatus.PENDING }
            for (monitoringItem in monitoringItems) {
                val workflowStr: String = if (!monitoringItem.workflowS3Key.isEmpty()) {
                    val s3Obj = s3Service.getObject(monitoringItem.workflowS3Key)
                    s3Obj.data.decodeToString()
                } else {
                    "{}"
                }

                val workflowInstance = workflowConverter.build(monitoringItem, workflowStr)
                workflowManager.execute(workflowInstance, workflowPersister)
            }

            // continue loading
            list = tableItemRepository.getList(tableName, list.nextPageToken)
        }
        TODO("Not yet implemented")
    }
}