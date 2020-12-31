package com.siyamand.aws.dynamodb.core.services

import com.siyamand.aws.dynamodb.core.entities.*
import com.siyamand.aws.dynamodb.core.repositories.ResourceRepository
import com.siyamand.aws.dynamodb.core.repositories.TableRepository

class MetadataServiceImpl(
        private  val resourceRepository: ResourceRepository,
        private val monitorConfigProvider: MonitorConfigProvider,
        private val tableRepository: TableRepository) : MetadataService {

    override fun getMonitoredTables(): List<ResourceEntity> {
        val returnValue = mutableListOf<ResourceEntity>()

        val tagName = monitorConfigProvider.getDynamodbTagName()
        val tagValue = monitorConfigProvider.getDynamodbTagValue()

        // fetch first batch
        var currentBatch = resourceRepository.getResources(tagName, tagValue, "")
        returnValue.addAll(currentBatch.items)

        // fetch next pages
        while (currentBatch.nextPageToken != null){
            currentBatch = resourceRepository.getResources(tagName, tagValue, currentBatch.nextPageToken)
            returnValue.addAll(currentBatch.items)
        }
        return  returnValue
    }
}