package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider
import com.siyamand.aws.dynamodb.core.common.initializeRepositories
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.MonitorStatus
import com.siyamand.aws.dynamodb.core.sdk.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.sdk.dynamodb.AttributeValueEntity
import com.siyamand.aws.dynamodb.core.sdk.dynamodb.TableItemRepository
import com.siyamand.aws.dynamodb.core.sdk.s3.S3Service
import com.siyamand.aws.dynamodb.core.workflow.WorkflowConverter
import com.siyamand.aws.dynamodb.core.workflow.WorkflowInstance
import com.siyamand.aws.dynamodb.core.workflow.WorkflowPersister
import com.siyamand.aws.dynamodb.core.workflow.WorkflowResultType
import java.nio.charset.StandardCharsets

class WorkflowPersisterImpl(
        private val tableItemRepository: TableItemRepository,
        private var credentialProvider: CredentialProvider,
        private val monitoringItemBuilder: MonitoringItemConverter,
        private val workflowConverter: WorkflowConverter,
        private val s3Service: S3Service,
        private val monitorConfigProvider: MonitorConfigProvider) : WorkflowPersister {

    override suspend fun load(id: String): WorkflowInstance {
        credentialProvider.initializeRepositories(tableItemRepository)
        val item = tableItemRepository.getItem(monitorConfigProvider.getMonitoringConfigMetadataTable(), mapOf("id" to AttributeValueEntity(id)))
        if (item.any()) {
            throw Exception("no item have been found")
        }
        val monitoringItem = monitoringItemBuilder.convertToAggregateEntity(item.first())
        val workflowObject = if (!monitoringItem.workflowS3Key.isEmpty())
            s3Service.getObject(monitoringItem.workflowS3Key)
        else null

        val workflowString = workflowObject?.data?.decodeToString() ?: "{}"
        return workflowConverter.build(monitoringItem, workflowString)
    }

    override suspend fun save(instance: WorkflowInstance) {
        credentialProvider.initializeRepositories(tableItemRepository)
        val items = tableItemRepository.getItem(monitorConfigProvider.getMonitoringConfigMetadataTable(), mapOf("id" to AttributeValueEntity(instance.id)))
        if (!items.any()) {
            throw Exception("no item have been found")
        }

        val item = items.first()
        val monitoringItem = monitoringItemBuilder.convertToAggregateEntity(item)

        if (instance.lastResult?.resultType == WorkflowResultType.WAITING) {
            monitoringItem.status = MonitorStatus.PENDING
        } else if (instance.lastResult?.resultType == WorkflowResultType.ERROR) {
            monitoringItem.status = MonitorStatus.ERROR
        } else if (instance.lastResult?.resultType == WorkflowResultType.SUCCESS) {
            if (instance.currentStep >= instance.steps.size - 1) {
                monitoringItem.status = MonitorStatus.ACTIVE
            } else {
                monitoringItem.status = MonitorStatus.PENDING
            }
        } else if (instance.lastResult?.resultType == WorkflowResultType.FINISH) {
            monitoringItem.status = MonitorStatus.ACTIVE
        }

        val workflowStr = workflowConverter.serialize(instance)
        val s3Object = s3Service.addObject("workflow-${monitoringItem.id}", "data/json", monitoringItem.id, workflowStr.toByteArray(StandardCharsets.UTF_8))
        monitoringItem.workflowS3Key = s3Object.key
        val newItem = monitoringItemBuilder.convert(item.tableName, monitoringItem)
        tableItemRepository.update(newItem)
    }

    override suspend fun threadSafe(){
        s3Service.threadSafe()
        this.credentialProvider = this.credentialProvider.threadSafe()
    }
}