package com.siyamand.aws.dynamodb.core.database

import com.siyamand.aws.dynamodb.core.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.rds.RdsRepository
import com.siyamand.aws.dynamodb.core.resource.ResourceRepository
import com.siyamand.aws.dynamodb.core.secretManager.SecretManagerRepository
import com.siyamand.aws.dynamodb.core.template.TemplateEngine
import com.siyamand.aws.dynamodb.core.workflow.WorkflowContext

class CreateDatabaseTableWorkflowStep(credentialProvider: CredentialProvider,
                                      databaseRepository: DatabaseRepository,
                                      resourceRepository: ResourceRepository,
                                      rdsRepository: RdsRepository,
                                      secretManagerRepository: SecretManagerRepository,
                                      private val templateEngine: TemplateEngine) : ExecuteStatementDatabaseWorkflowStep(
        credentialProvider,
        databaseRepository,
        resourceRepository,
        rdsRepository,
        secretManagerRepository
) {
    override val name: String = "CreateDatabaseTable"

    override fun customizeSql(context: WorkflowContext, params: Map<String, String>, sql: String): Array<String> {
        if (!params.containsKey("tableNames")) {
            throw Exception("tableNames param is mandatory")
        }

        val statements = (params["tableNames"])!!
                .split(",")
                .map { templateEngine.execute(sql, mapOf("tableName" to it.trim())) }
        return statements.toTypedArray()
    }
}