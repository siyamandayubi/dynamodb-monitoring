package com.siyamand.aws.dynamodb.core.workflow.templates

import com.siyamand.aws.dynamodb.core.workflow.*

class AggregateSimpleMysqlDatabaseTemplate(private val allSteps: Iterable<WorkflowStep>) : WorkflowTemplate {
    override val version: Int = 1
    override suspend fun getSteps(): List<WorkflowStepInstance> {
        val steps = listOf(
                WorkflowStepInstance(
                        "AddLambdaLayer", allSteps.first { it.name == "AddLambdaLayer" }, WorkflowStepStatus.INITIAL,
                        mapOf(
                                Keys.LAMBDA_LAYER_PATH to "lambda/layers/mysql/mysql-layer.zip",
                                Keys.LAMBDA_LAYER_NAME to "mysql-layer",
                                "description" to "The layer contains mysql module and helper functions to execute queries and loading secrets from secret manager"),
                ),
                WorkflowStepInstance(
                        "AddLambdaLayer", allSteps.first { it.name == "AddLambdaLayer" }, WorkflowStepStatus.INITIAL,
                        mapOf(
                                Keys.LAMBDA_LAYER_PATH to "lambda/layers/crypto/crypto.zip",
                                Keys.LAMBDA_LAYER_NAME to "crypto-layer",
                                "description" to "The layer contains crypto module which can be used in generating hash values"),
                ),
                WorkflowStepInstance("CreateSecret", allSteps.first { it.name == "CreateSecret" }, WorkflowStepStatus.INITIAL, mapOf()),
                WorkflowStepInstance("CreateRdsInstance", allSteps.first { it.name == "CreateRdsInstance" }, WorkflowStepStatus.INITIAL, mapOf()),
                WorkflowStepInstance("CreateDatabase", allSteps.first { it.name == "CreateDatabase" }, WorkflowStepStatus.INITIAL, mapOf()),
                WorkflowStepInstance("CreateRdsProxy", allSteps.first { it.name == "CreateRdsProxy" }, WorkflowStepStatus.INITIAL, mapOf()),
                WorkflowStepInstance("CreateRdsProxyTargetGroup", allSteps.first { it.name == "CreateRdsProxyTargetGroup" }, WorkflowStepStatus.INITIAL, mapOf()),
                WorkflowStepInstance("CreateDatabaseTable", allSteps.first { it.name == "CreateDatabaseTable" }, WorkflowStepStatus.INITIAL, mapOf(
                        "tableName" to "AggregateTable",
                        "sql_file" to "database/aggregate-table.sql"
                ))
        )

        steps.forEach {
            it.step.initialize()
        }

        return steps
    }

    override fun getRequiredParameters(): List<RequiredWorkflowParameter> {
        return listOf(RequiredWorkflowParameter(Keys.DATABASE_NAME, WorkflowParameterType.STRING))
    }


    override val name: String = "AggregateSimpleMysqlDatabaseTemplate"
}