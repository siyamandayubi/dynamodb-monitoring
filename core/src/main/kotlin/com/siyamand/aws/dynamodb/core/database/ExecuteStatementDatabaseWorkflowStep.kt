package com.siyamand.aws.dynamodb.core.database

import com.siyamand.aws.dynamodb.core.sdk.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.sdk.rds.RdsRepository
import com.siyamand.aws.dynamodb.core.sdk.resource.ResourceRepository
import com.siyamand.aws.dynamodb.core.sdk.secretManager.SecretManagerRepository
import com.siyamand.aws.dynamodb.core.workflow.*
import java.nio.file.Files
import java.nio.file.Paths

open class ExecuteStatementDatabaseWorkflowStep(
        credentialProvider: CredentialProvider,
        databaseRepository: DatabaseRepository,
        resourceRepository: ResourceRepository,
        rdsRepository: RdsRepository,
        secretManagerRepository: SecretManagerRepository) : DatabaseWorkflowStep(credentialProvider, databaseRepository, resourceRepository, rdsRepository, secretManagerRepository) {
    override val name: String = "ExecuteStatementDatabase"

    override suspend fun execute(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {

        // check sql template Address
        if (!params.containsKey("sql_file")) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "sql_file param is required")
        }
        val uri = javaClass.classLoader.getResource(params["sql_file"]).toURI()
        var sql = Files.readString(Paths.get(uri))

        val sqls = customizeSql(instance.context, params, sql)

        return execute(instance.context, params) {

            databaseConnectionEntity ->
            run {
                sqls.forEach { sql ->
                    databaseRepository.executeSql(databaseConnectionEntity, sql)
                }
            }

            WorkflowResult(WorkflowResultType.SUCCESS, mapOf(), "")
        }
    }

    protected open fun customizeSql(context: WorkflowContext, params: Map<String, String>, sql: String): Array<String> {
        return arrayOf(sql)
    }

    override suspend fun isWaiting(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        return execute(instance, owner, params)
    }

    override suspend fun initialize() {
        this.credentialProvider = credentialProvider.threadSafe()
    }
}

