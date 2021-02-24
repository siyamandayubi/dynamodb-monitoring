package com.siyamand.aws.dynamodb.core.dynamodb

import com.siyamand.aws.dynamodb.core.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.common.initializeRepositories
import com.siyamand.aws.dynamodb.core.resource.ResourceRepository
import com.siyamand.aws.dynamodb.core.workflow.*

class EnableDynamoDbStreamWorkflowStep(private var credentialProvider: CredentialProvider,
                                       private val resourceRepository: ResourceRepository,
                                       private val tableRepository: TableRepository) : WorkflowStep() {
    override val name: String = "EnableDynamoDbStream"

    override suspend fun execute(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        val context = instance.context

        if (!context.sharedData.containsKey(Keys.SOURCE_DYNAMODB_ARN)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "no ${Keys.SOURCE_DYNAMODB_ARN} in shared data")
        }

        credentialProvider.initializeRepositories(tableRepository, resourceRepository)

        val tableResource = resourceRepository.convert(context.sharedData[Keys.SOURCE_DYNAMODB_ARN]!!)

        val tableName = if (tableResource.resource.contains("/")) tableResource.resource.split("/")[1] else tableResource.resource

        var table = tableRepository.getDetail(tableName)
                ?: return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "no table found. ARN:${Keys.SOURCE_DYNAMODB_ARN}")

        if (!table.streamEnabled) {
            table = tableRepository.enableStream(table.tableName)!!
        }

        if (table.latestStream == null) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "table doesn't have any stream. ARN:${Keys.SOURCE_DYNAMODB_ARN}")
        }

        context.sharedData[Keys.STREAM__DYNAMODB_ARN] = table.latestStream!!.arn
        return WorkflowResult(WorkflowResultType.SUCCESS, mapOf(Keys.STREAM__DYNAMODB_ARN to table.latestStream!!.arn), "")
    }

    override suspend fun isWaiting(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        return execute(instance, owner, params)
    }

    override suspend fun initialize() {
        this.credentialProvider = credentialProvider.threadSafe()
    }
}