package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider
import com.siyamand.aws.dynamodb.core.common.initializeRepositories
import com.siyamand.aws.dynamodb.core.dynamodb.*
import com.siyamand.aws.dynamodb.core.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.resource.ResourceRepository
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
        private val workflowConverter: WorkflowConverter,
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

    override suspend fun getMonitoredTables(): List<MonitoringBaseEntity<AggregateMonitoringEntity>> {
        val returnValue = mutableListOf<MonitoringBaseEntity<AggregateMonitoringEntity>>()

        credentialProvider.initializeRepositories(tableItemRepository)

        val tableName = monitorConfigProvider.getMonitoringConfigMetadataTable()
        var currentBatch = tableItemRepository.getList(tableName, null)
        // fetch first batch
        returnValue.addAll(currentBatch.items.map { monitoringItemConverter.convertToAggregateEntity(it) })

        // fetch next pages
        while (currentBatch.nextPageToken != null && currentBatch.nextPageToken?.any() == true) {
            currentBatch = tableItemRepository.getList(tableName, null)
            returnValue.addAll(currentBatch.items.map { monitoringItemConverter.convertToAggregateEntity(it) })
        }
        return returnValue
    }

    override suspend fun resumeWorkflow(id: String) {
        credentialProvider.initializeRepositories(tableRepository, tableItemRepository, resourceRepository)

        val tableName = monitorConfigProvider.getMonitoringConfigMetadataTable()
        if (tableName.isNullOrEmpty()) {
            throw Exception("No config name for Monitoring Dynamodb table")
        }
        var workflow: TableItemEntity? = tableItemRepository.getItem(tableName, mapOf("id" to AttributeValueEntity(id))).firstOrNull()
                ?: throw Exception("No workflow has been found. id= $id")

        var monitoringItem = monitoringItemConverter.convertToAggregateEntity(workflow!!)
        if (monitoringItem.status != MonitorStatus.PENDING && monitoringItem.status != MonitorStatus.INITIAL) {
            throw Exception("Workflow with id=$id has final status.")
        }
        val workflowInstance = workflowConverter.build(monitoringItem)

        val task = Runnable {
            runBlocking {
                workflowManager.execute(workflowInstance, workflowPersister)
            }
        }

        scheduler.schedule(task, scheduler.clock.instant().plusMillis(500))
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