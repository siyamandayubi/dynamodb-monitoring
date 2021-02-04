package com.siyamand.aws.dynamodb.core.workflow

import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider
import com.siyamand.aws.dynamodb.core.dynamodb.AttributeValueEntity
import com.siyamand.aws.dynamodb.core.dynamodb.TableItemRepository
import com.siyamand.aws.dynamodb.core.monitoring.MonitoringItemBuilder

class WorkflowPersisterImpl(
        private val tableItemRepository: TableItemRepository,
        private val monitoringItemBuilder: MonitoringItemBuilder,
        private val workflowBuilder: WorkflowBuilder,
        private val monitorConfigProvider: MonitorConfigProvider) : WorkflowPersister {
    override suspend fun load(id: String): WorkflowInstance {
        val item = tableItemRepository.getItem(monitorConfigProvider.getMonitoringConfigMetadataTable(), mapOf("id" to AttributeValueEntity(id)))
        if (item!!.any()){
            throw Exception("no item habe been found")
        }
        val monitoringItem = monitoringItemBuilder.build(item.first())
        return workflowBuilder.build(monitoringItem)
    }

    override suspend fun save(instance: WorkflowInstance) {
        val item = tableItemRepository.getItem(monitorConfigProvider.getMonitoringConfigMetadataTable(), mapOf("id" to AttributeValueEntity(instance.id)))
        if (item!!.any()){
            throw Exception("no item habe been found")
        }

        val monitoringItem = monitoringItemBuilder.build(item.first())
        monitoringItem.workflow = workflowBuilder.serialize(instance)
        val newItem = monitoringItemBuilder.convert(monitoringItem)
        tableItemRepository.update(newItem)
    }
}