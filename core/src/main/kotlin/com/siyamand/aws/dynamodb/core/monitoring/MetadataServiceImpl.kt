package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider
import com.siyamand.aws.dynamodb.core.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.resource.ResourceRepository
import com.siyamand.aws.dynamodb.core.dynamodb.TableRepository

class MetadataServiceImpl(
        private  val resourceRepository: ResourceRepository,
        private val monitorConfigProvider: MonitorConfigProvider,
        private val tableRepository: TableRepository) : MetadataService {

    override fun getMonitoredTables(): List<ResourceEntity> {
        val returnValue = mutableListOf<ResourceEntity>()

        val tagName = monitorConfigProvider.getMonitoringVersionTagName()
        val tagValue = monitorConfigProvider.getMonitoringVersionValue()

        // fetch first batch
        var currentBatch = resourceRepository.getResources(tagName, tagValue,null, "")
        returnValue.addAll(currentBatch.items)

        // fetch next pages
        while (currentBatch.nextPageToken != null){
            currentBatch = resourceRepository.getResources(tagName, tagValue, null, currentBatch.nextPageToken)
            returnValue.addAll(currentBatch.items)
        }
        return  returnValue
    }
}