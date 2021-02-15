package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider
import com.siyamand.aws.dynamodb.core.common.initializeRepositories
import com.siyamand.aws.dynamodb.core.dynamodb.TableDetailEntity
import com.siyamand.aws.dynamodb.core.dynamodb.TableItemRepository
import com.siyamand.aws.dynamodb.core.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.resource.ResourceRepository
import com.siyamand.aws.dynamodb.core.dynamodb.TableRepository
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.AggregateMonitoringEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.MonitorStatus
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.MonitoringBaseEntity
import com.siyamand.aws.dynamodb.core.workflow.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import org.springframework.scheduling.TaskScheduler

class MetadataServiceImpl(
        private val resourceRepository: ResourceRepository,
        private val monitorConfigProvider: MonitorConfigProvider,
        private val monitoringTableBuilder: MonitoringTableBuilder,
        private val credentialProvider: CredentialProvider,
        private val workflowBuilder: WorkflowBuilder,
        private val workflowManager: WorkflowManager,
        private val workflowPersister: WorkflowPersister,
        private val tableItemRepository: TableItemRepository,
        private val monitoringItemConverter: MonitoringItemConverter,
        private val tableRepository: TableRepository,
        private val scheduler: TaskScheduler) : MetadataService {

    override fun getMonitoredTables(): List<ResourceEntity> {
        val returnValue = mutableListOf<ResourceEntity>()

        val tagName = monitorConfigProvider.getMonitoringVersionTagName()
        val tagValue = monitorConfigProvider.getMonitoringVersionValue()

        // fetch first batch
        var currentBatch = resourceRepository.getResources(tagName, tagValue, null, "")
        returnValue.addAll(currentBatch.items)

        // fetch next pages
        while (currentBatch.nextPageToken != null) {
            currentBatch = resourceRepository.getResources(tagName, tagValue, null, currentBatch.nextPageToken)
            returnValue.addAll(currentBatch.items)
        }
        return returnValue
    }

    override suspend fun startWorkflow(sourceTableName: String, workflowName: String, entity: AggregateMonitoringEntity) {
        credentialProvider.initializeRepositories(tableRepository, tableItemRepository, resourceRepository)
        val table = getOrCreateMonitoringTable()
        var workflowInstance = workflowBuilder.create(workflowName, mapOf(
                Keys.DATABASE_NAME to entity.databaseName,
                "tableName" to (entity.fields.firstOrNull()?.tableName ?: "")))

        val monitoringEntity = MonitoringBaseEntity<AggregateMonitoringEntity>(
                workflowInstance.id,
                sourceTableName,
                workflowName,
                MonitorStatus.INITIAL,
                workflowInstance.template.version,
                "",
                entity
        )

        tableItemRepository.add(monitoringItemConverter.convert(table.tableName, monitoringEntity))
        val task = Runnable {
            runBlocking {
                workflowManager.execute(workflowInstance, workflowPersister)
            }
        }

        scheduler.schedule(task, scheduler.clock.instant().plusMillis(500))
    }

    suspend fun getOrCreateMonitoringTable(): TableDetailEntity {
        val tableName = monitorConfigProvider.getMonitoringConfigMetadataTable()
        if (tableName.isNullOrEmpty()) {
            throw Exception("No config name for Monitoring Dynamodb table")
        }

        var table = tableRepository.getDetail(tableName)
        if (table != null) {
            return table
        }

        return tableRepository.add(monitoringTableBuilder.build(tableName))
    }

    suspend fun initializeRepositories() {
        val credential = credentialProvider.getCredential()
                ?: throw SecurityException("No Credential has been provided");

        tableRepository.initialize(credential, credentialProvider.getRegion());

    }
}