package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider
import com.siyamand.aws.dynamodb.core.dynamodb.AttributeValueEntity
import com.siyamand.aws.dynamodb.core.dynamodb.TableItemRepository
import com.siyamand.aws.dynamodb.core.workflow.WorkflowConverter
import com.siyamand.aws.dynamodb.core.workflow.WorkflowInstance
import com.siyamand.aws.dynamodb.core.workflow.WorkflowPersister

class WorkflowPersisterImpl(
        private val tableItemRepository: TableItemRepository,
        private val monitoringItemBuilder: MonitoringItemConverter,
        private val workflowConverter: WorkflowConverter,
        private val monitorConfigProvider: MonitorConfigProvider) : WorkflowPersister {
    override suspend fun load(id: String): WorkflowInstance {
        val item = tableItemRepository.getItem(monitorConfigProvider.getMonitoringConfigMetadataTable(), mapOf("id" to AttributeValueEntity(id)))
        if (item!!.any()){
            throw Exception("no item habe been found")
        }
        val monitoringItem = monitoringItemBuilder.convertToAggregateEntity(item.first())
        return workflowConverter.build(monitoringItem)
    }

    override suspend fun save(instance: WorkflowInstance) {
        val item = tableItemRepository.getItem(monitorConfigProvider.getMonitoringConfigMetadataTable(), mapOf("id" to AttributeValueEntity(instance.id)))
        if (item!!.any()){
            throw Exception("no item have been found")
        }

        val monitoringItem = monitoringItemBuilder.convertToAggregateEntity(item.first())
        monitoringItem.workflow = workflowConverter.serialize(instance)
        val newItem = monitoringItemBuilder.convert(monitoringItem.sourceTable, monitoringItem)
        tableItemRepository.update(newItem)
    }
}