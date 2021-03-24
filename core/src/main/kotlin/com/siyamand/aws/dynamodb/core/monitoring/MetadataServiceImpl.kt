package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider
import com.siyamand.aws.dynamodb.core.common.PageResultEntity
import com.siyamand.aws.dynamodb.core.common.initializeRepositories
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.AggregateMonitoringEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.MonitoringBaseEntity
import com.siyamand.aws.dynamodb.core.sdk.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.sdk.dynamodb.AttributeValueEntity
import com.siyamand.aws.dynamodb.core.sdk.dynamodb.TableItemRepository

class MetadataServiceImpl(private val tableItemRepository: TableItemRepository,
                          private val credentialProvider: CredentialProvider,
                          private val monitoringTableBuilder: MonitoringTableBuilder,
                          private val monitorConfigProvider: MonitorConfigProvider,
                          private val monitoringItemConverter: MonitoringItemConverter) : MetadataService {

    override suspend fun getMonitoredTables(startKey: String): PageResultEntity<MonitoringBaseEntity<AggregateMonitoringEntity>> {
        credentialProvider.initializeRepositories(tableItemRepository)

        val tableName = monitorConfigProvider.getMonitoringConfigMetadataTable()
        val startKeyMap = mutableMapOf<String, AttributeValueEntity>()

        if (!startKey.isNullOrEmpty()) {
            startKeyMap[monitoringTableBuilder.keyName] = AttributeValueEntity(startKey)
        }

        var batch = tableItemRepository.getList(tableName, startKeyMap)

        var nextKey = ""
        if (batch.nextPageToken?.containsKey(monitoringTableBuilder.keyName) == true) {
            nextKey = batch.nextPageToken!![monitoringTableBuilder.keyName]!!.stringValue ?: ""
        }
        return PageResultEntity(
                batch.items.map { monitoringItemConverter.convertToAggregateEntity(it) },
                nextKey
        )
    }

    suspend fun getMonitoringItemResources() {

    }
}