package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider
import com.siyamand.aws.dynamodb.core.dynamodb.AttributeValueEntity
import com.siyamand.aws.dynamodb.core.dynamodb.TableItemRepository
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.MonitorStatus
import com.siyamand.aws.dynamodb.core.workflow.*

class WorkflowPersisterImpl(
        private val tableItemRepository: TableItemRepository,
        private val monitoringItemBuilder: MonitoringItemConverter,
        private val workflowConverter: WorkflowConverter,
        private val monitorConfigProvider: MonitorConfigProvider) : WorkflowPersister {
    override suspend fun load(id: String): WorkflowInstance {
        val item = tableItemRepository.getItem(monitorConfigProvider.getMonitoringConfigMetadataTable(), mapOf("id" to AttributeValueEntity(id)))
        if (item!!.any()) {
            throw Exception("no item habe been found")
        }
        val monitoringItem = monitoringItemBuilder.convertToAggregateEntity(item.first())
        return workflowConverter.build(monitoringItem)
    }

    override suspend fun save(instance: WorkflowInstance) {
        val items = tableItemRepository.getItem(monitorConfigProvider.getMonitoringConfigMetadataTable(), mapOf("id" to AttributeValueEntity(instance.id)))
        if (!items?.any()) {
            throw Exception("no item have been found")
        }

        val item = items.first()
        val monitoringItem = monitoringItemBuilder.convertToAggregateEntity(item)

        if (instance.lastResult?.resultType  == WorkflowResultType.WAITING){
            monitoringItem.status = MonitorStatus.PENDING
        }
        else if (instance.lastResult?.resultType == WorkflowResultType.ERROR){
            monitoringItem.status = MonitorStatus.ERROR
        }
        else if (instance.lastResult?.resultType == WorkflowResultType.SUCCESS){
            if (instance.currentStep == instance.steps.size -1){
                monitoringItem.status = MonitorStatus.ACTIVE
            }
            else{
                monitoringItem.status = MonitorStatus.PENDING
            }
        }
        else if (instance.lastResult?.resultType == WorkflowResultType.FINISH){
            monitoringItem.status = MonitorStatus.ACTIVE
        }

        monitoringItem.workflow = workflowConverter.serialize(instance)
        val newItem = monitoringItemBuilder.convert(item.tableName, monitoringItem)
        tableItemRepository.update(newItem)
    }
}