package com.siyamand.aws.dynamodb.core.workflow.templates

import com.siyamand.aws.dynamodb.core.role.RoleBuilder
import com.siyamand.aws.dynamodb.core.workflow.*

class AggregateSimpleMysqlDatabaseTemplate(
        private val roleBuilder: RoleBuilder,
        private val allSteps: Iterable<WorkflowStep>) : WorkflowTemplate {
    override val version: Int = 1
    override suspend fun getSteps(workflowContext: WorkflowContext): List<WorkflowStepInstance> {
        val steps = listOf(
                WorkflowStepInstance(
                        "AddLambdaLayer-Mysql", allSteps.first { it.name == "AddLambdaLayer" }, WorkflowStepStatus.INITIAL,
                        mapOf(
                                Keys.LAMBDA_LAYER_PATH to "lambda/layers/mysql/mysql.zip",
                                Keys.LAMBDA_LAYER_NAME to "mysql-layer",
                                "output" to "mysql-layer",
                                "description" to "The layer contains mysql module and helper functions to execute queries and loading secrets from secret manager"),
                ),
                WorkflowStepInstance(
                        "AddLambdaLayer", allSteps.first { it.name == "AddLambdaLayer" }, WorkflowStepStatus.INITIAL,
                        mapOf(
                                Keys.LAMBDA_LAYER_PATH to "lambda/layers/crypto/crypto.zip",
                                Keys.LAMBDA_LAYER_NAME to "crypto-layer",
                                "output" to "crypto-layer",
                                "description" to "The layer contains crypto module which can be used in generating hash values"),
                ),
                WorkflowStepInstance("CreateSecret", allSteps.first { it.name == "CreateSecret" }, WorkflowStepStatus.INITIAL, mapOf()),
                WorkflowStepInstance("CreateRdsInstance", allSteps.first { it.name == "CreateRdsInstance" }, WorkflowStepStatus.INITIAL, mapOf(
                        "dbInstanceName" to (workflowContext.sharedData["dbInstanceName"] ?: "")
                )),
                WorkflowStepInstance("CreateDatabase", allSteps.first { it.name == "CreateDatabase" }, WorkflowStepStatus.INITIAL, mapOf()),
              /*  WorkflowStepInstance("CreateRdsProxy", allSteps.first { it.name == "CreateRdsProxy" }, WorkflowStepStatus.INITIAL, mapOf()),
                WorkflowStepInstance("CreateRdsProxyTargetGroup", allSteps.first { it.name == "CreateRdsProxyTargetGroup" }, WorkflowStepStatus.INITIAL, mapOf(
                        Keys.PROXY_TARGET_GROUP_NAME to (workflowContext.sharedData["dbInstanceName"] ?: "")
                )),*/
                WorkflowStepInstance("CreateDatabaseTable", allSteps.first { it.name == "CreateDatabaseTable" }, WorkflowStepStatus.INITIAL, mapOf(
                        "tableNames" to (workflowContext.sharedData["tableNames"] ?: ""),
                        "sql_file" to "database/aggregate-table.sql"
                )),
                WorkflowStepInstance("AggregateMonitoringEntityCodeGenerator", allSteps.first { it.name == "AggregateMonitoringEntityCodeGenerator" }, WorkflowStepStatus.INITIAL, mapOf(
                        "code-path" to "lambdaTemplates/aggregateTemplate.ftl",
                        Keys.DATABASE_NAME to (workflowContext.sharedData["dbInstanceName"] ?: "")
                )),
                WorkflowStepInstance("AddLambdaFunction", allSteps.first { it.name == "AddLambdaFunction" }, WorkflowStepStatus.INITIAL, mapOf(
                        "layers" to "mysql-layer,crypto-layer",
                        Keys.LAMBDA_ROLE to roleBuilder.lambdaRoleName,
                        "name" to (workflowContext.sharedData["lambda-name"] ?: "defaultFuncion")
                )),
                WorkflowStepInstance("AddLambdaEventSource", allSteps.first { it.name == "AddLambdaEventSource" }, WorkflowStepStatus.INITIAL, mapOf()),
        )

        steps.forEach {
            it.step.initialize()
        }

        return steps
    }

    override fun getRequiredParameters(): List<RequiredWorkflowParameter> {
        return listOf(RequiredWorkflowParameter(Keys.DATABASE_NAME, WorkflowParameterType.STRING),
                RequiredWorkflowParameter(Keys.SOURCE_DYNAMODB_ARN, WorkflowParameterType.STRING),
                RequiredWorkflowParameter("tableNames", WorkflowParameterType.STRING),
                RequiredWorkflowParameter("lambda-name", WorkflowParameterType.STRING),
                RequiredWorkflowParameter("dbInstanceName", WorkflowParameterType.STRING))
    }


    override val name: String = "AggregateSimpleMysqlDatabaseTemplate"
}