package com.siyamand.aws.dynamodb.core.workflow.templates

import com.siyamand.aws.dynamodb.core.workflow.Keys
import com.siyamand.aws.dynamodb.core.workflow.WorkflowInstance
import com.siyamand.aws.dynamodb.core.workflow.WorkflowStep
import com.siyamand.aws.dynamodb.core.workflow.WorkflowTemplate

class AggregateSimpleMysqlDatabaseTemplate(private val allSteps: Iterable<WorkflowStep>) : WorkflowTemplate {
    override val version: Int = 1
    override val name: String = "AggregateSimpleMysqlDatabaseTemplate"
    private var _steps: List<WorkflowStep>? = null
    override val steps: List<WorkflowStep>
        get() {
            if (_steps == null) {
                _steps = listOf(
                        allSteps.first { it.name == "AddLambdaLayer" },
                        allSteps.first { it.name == "CreateSecret" },
                        allSteps.first { it.name == "CreateRdsInstance" },
                        allSteps.first { it.name == "CreateDatabase" },
                )
            }
            return _steps!!
        }
    override val defaultParams: Map<String, Map<String, String>>
        get() {
            return mapOf("AddLambdaLayer" to mapOf(
                    Keys.LAMBDA_LAYER_PATH to "lambda/layers/mysql/mysql-layer.zip",
                    Keys.LAMBDA_LAYER_NAME to "mysql-layer",
                    "description" to "The layer contains mysql module and helper functions to execute queries and loading secrets from secret manager"),
                    )
        }
}