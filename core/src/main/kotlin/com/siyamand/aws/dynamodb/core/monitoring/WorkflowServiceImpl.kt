package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.sdk.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider
import com.siyamand.aws.dynamodb.core.common.initializeRepositories
import com.siyamand.aws.dynamodb.core.sdk.dynamodb.*
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.AggregateMonitoringEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.MonitorStatus
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.MonitoringBaseEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.StartMonitoringWorkflowEntity
import com.siyamand.aws.dynamodb.core.sdk.s3.S3Service
import com.siyamand.aws.dynamodb.core.workflow.*
import kotlinx.coroutines.runBlocking
import org.springframework.scheduling.TaskScheduler

class WorkflowServiceImpl(
        private val s3Service: S3Service,
        private val prerequisiteService: PrerequisiteReadonlyService,
        private val workflowConverter: WorkflowConverter,
        private val monitorConfigProvider: MonitorConfigProvider,
        private val credentialProvider: CredentialProvider,
        private val workflowBuilder: WorkflowBuilder,
        private val workflowManager: WorkflowManager,
        private val workflowPersister: WorkflowPersister,
        private val tableItemRepository: TableItemRepository,
        private val monitoringItemConverter: MonitoringItemConverter,
        private val tableRepository: TableRepository,
        private val scheduler: TaskScheduler) : WorkflowService {

     override suspend fun resumeWorkflow(id: String) {
        credentialProvider.initializeRepositories(tableRepository, tableItemRepository)

        val tableName = monitorConfigProvider.getMonitoringConfigMetadataTable()
        if (tableName.isNullOrEmpty()) {
            throw Exception("No config name for Monitoring Dynamodb table")
        }
        var tableItem: TableItemEntity? = tableItemRepository.getItem(tableName, mapOf("id" to AttributeValueEntity(id))).firstOrNull()
                ?: throw Exception("No workflow has been found. id= $id")

        var monitoringItem = monitoringItemConverter.convertToAggregateEntity(tableItem!!)
        if (monitoringItem.status != MonitorStatus.PENDING &&
                monitoringItem.status != MonitorStatus.INITIAL &&
                monitoringItem.status != MonitorStatus.ERROR) {
            throw Exception("Workflow with id=$id has final status.")
        }
        val workflowStr: String = if (!monitoringItem.workflowS3Key.isNullOrEmpty()) {
            val s3Obj = s3Service.getObject(monitoringItem.workflowS3Key)
            s3Obj.data.decodeToString()
        } else {
            "{}"
        }
        val workflowInstance = workflowConverter.build(monitoringItem, workflowStr)

        workflowPersister.threadSafe()
        val task = Runnable {
            runBlocking {
                workflowManager.execute(workflowInstance, monitoringItem, workflowPersister)
            }
        }

        scheduler.schedule(task, scheduler.clock.instant().plusMillis(500))
    }

    override suspend fun startWorkflow(model: StartMonitoringWorkflowEntity) {
        credentialProvider.initializeRepositories(tableRepository, tableItemRepository)
        val monitoringTable = prerequisiteService.getMonitoringTable()
                ?: throw Exception("monitoring table has not been created")

        val definition = model.definition!!
        val sourceTable = tableRepository.getDetail(model.sourceTableName)
                ?: throw Exception("Source table '${model.sourceTableName}' doesn't exists.")

        var workflowInstance = workflowBuilder.create(model.workflowName, mapOf(
                Keys.DATABASE_NAME to definition.databaseName,
                "lambda-name" to model.lambdaName,
                Keys.DB_INSTANCE_CLASS to model.instanceClass,
                Keys.SOURCE_DYNAMODB_ARN to (sourceTable.arn),
                "dbInstanceName" to model.rdsInstanceNamePrefix,
                "instancesCount" to model.instancesCount.toString(),
                "tableNames" to (definition.groups.map { it.tableName }.joinToString(separator = ","))))

        val monitoringEntity = MonitoringBaseEntity<AggregateMonitoringEntity>(
                workflowInstance.id,
                model.sourceTableName,
                model.workflowName,
                MonitorStatus.INITIAL,
                workflowInstance.template.version,
                "",
                definition
        )

        tableItemRepository.add(monitoringItemConverter.convert(monitoringTable.tableName, monitoringEntity))
        workflowPersister.threadSafe()
        val task = Runnable {
            runBlocking {
                workflowManager.execute(workflowInstance, monitoringEntity, workflowPersister)
            }
        }

        scheduler.schedule(task, scheduler.clock.instant().plusMillis(500))
    }

    suspend fun initializeRepositories() {
        val credential = credentialProvider.getCredential()
                ?: throw SecurityException("No Credential has been provided");

        tableRepository.initialize(credential, credentialProvider.getRegion());

    }
}