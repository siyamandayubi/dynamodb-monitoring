package com.siyamand.aws.dynamodb.core.workflow.templates

import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider
import com.siyamand.aws.dynamodb.core.sdk.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.workflow.*

class AggregateSimpleMysqlDatabaseTemplate(
        private val monitorConfigProvider: MonitorConfigProvider,
        private val credentialProvider: CredentialProvider,
        private val allSteps: Iterable<WorkflowStep>) : WorkflowTemplate {
    override val version: Int = 1
    override suspend fun getSteps(workflowContext: WorkflowContext): List<WorkflowStepInstance> {
        val steps: MutableList<WorkflowStepInstance> = mutableListOf()

        fun addStep(identifier: String, stepName: String, params: Map<String, String> = mapOf(), stepStatus: WorkflowStepStatus = WorkflowStepStatus.INITIAL) {
            steps.add(WorkflowStepInstance(identifier, allSteps.first { it.name == stepName }, stepStatus, params))
        }

        addStep("AddLambdaLayer-Mysql", "AddLambdaLayer", mapOf(
                Keys.LAMBDA_LAYER_PATH to "lambda/layers/mysql/mysql.zip",
                Keys.LAMBDA_LAYER_NAME to "mysql-layer",
                "output" to "mysql-layer",
                "description" to "The layer contains mysql module and helper functions to execute queries and loading secrets from secret manager"))
        addStep("AddLambdaLayer-aggretate-v1", "AddLambdaLayer", mapOf(
                Keys.LAMBDA_LAYER_PATH to "lambda/layers/aggretate_v1/aggregate_v1.zip",
                Keys.LAMBDA_LAYER_NAME to "aggregate-v1-layer",
                "output" to "aggregate-v1-layer",
                "description" to "The layer contains helper functions to generate sql statement from dynamodb change stream"))
        addStep(
                "AddLambdaLayer-Crypto", "AddLambdaLayer",
                mapOf(
                        Keys.LAMBDA_LAYER_PATH to "lambda/layers/crypto/crypto.zip",
                        Keys.LAMBDA_LAYER_NAME to "crypto-layer",
                        "output" to "crypto-layer",
                        "description" to "The layer contains crypto module which can be used in generating hash values"),
        )

        addStep("CreateSecret", "CreateSecret")
        addStep("AssignCounter", "Assign", mapOf("newValue" to "0", "variable" to "counter"))
        addStep("AssignRdsList", "Assign", mapOf("newValue" to "", "variable" to "rdsList"))
        addStep("AssignProxyList", "Assign", mapOf("newValue" to "", "variable" to "proxyList"))
        addStep("StartLoop", "IfElse", mapOf("else" to "RdsConfigBuilder",
                "condition" to "<#assign max = instancesCount?number>\n" +
                        "<#assign num = counter?number>\n" +
                        "<#if (num < max)>\n" +
                        "true\n" +
                        "<#else>\n" +
                        "false" +
                        "</#if>"))

        addStep("RemoveRdsArn", "Remove",
                mapOf("variable" to "${Keys.RDS_ARN_KEY},${Keys.PROXY_ARN_KEY},${Keys.PROXY_NAME}, ${Keys.PROXY_TARGET_GROUP_ARN}"))
        addStep("IncreaseCounter", "Assign", mapOf("newValue" to "<#assign x = counter?number>\${x + 1}", "variable" to "counter"))
        addStep("AssignInstanceName", "Assign", mapOf("newValue" to "\${dbInstanceName}-\${counter}", "variable" to "vdbInstanceName"))
        addStep("CreateRdsInstance", "CreateRdsInstance", mapOf(
                "dbInstanceName" to "vdbInstanceName")
        )
        addStep("AssignRdsList", "Assign", mapOf("newValue" to "\${rdsList},\${${Keys.RDS_ARN_KEY}}", "variable" to "rdsList"))

        addStep("CreateDatabase", "CreateDatabase")
        addStep("CreateRdsProxy", "CreateRdsProxy")
        addStep("AssignProxyList", "Assign", mapOf("newValue" to "\${proxyList},\${${Keys.PROXY_NAME}}", "variable" to "proxyList"))
        addStep("CreateRdsProxyTargetGroup", "CreateRdsProxyTargetGroup")
        addStep("CreateDatabaseTable", "CreateDatabaseTable", mapOf(
                "tableNames" to (workflowContext.sharedData["tableNames"] ?: ""),
                "sql_file" to "database/aggregate-table.sql"
        ))
        addStep("Jump", "Jump", mapOf("target" to "StartLoop"))
        addStep("RdsConfigBuilder", "RdsConfigBuilder", mapOf(
                "output" to "config",
                "proxyList" to "proxyList",
                Keys.DATABASE_NAME to (workflowContext.sharedData[Keys.DATABASE_NAME] ?: "")))
/*        addStep("CreateAppConfig", "CreateAppConfig", mapOf(
                "applicationName" to workflowContext.sharedData["dbInstanceName"]!!,
                "environmentName" to "production",
                "deploymentStrategyName" to "simple",
                "profileName" to "workflowContext.sharedData[\"dbInstanceName\"]!!",
                "appConfigContent" to "config"))*/
        addStep("AggregateMonitoringEntityCodeGenerator", "AggregateMonitoringEntityCodeGenerator", mapOf(
                "code-path" to "lambdaTemplates/aggregateTemplate-v2.ftl",
                "dbConfig" to "config",
                Keys.DATABASE_NAME to (workflowContext.sharedData[Keys.DATABASE_NAME] ?: "")
        ))
        addStep("AssignAppConfigArn",
                "Assign",
                mapOf("variable" to "appConfigLayerArn", "newValue" to monitorConfigProvider.getAppConfigLayerArn(credentialProvider.getRegion())))
        addStep("AddLambdaFunction", "AddLambdaFunction", mapOf(
                "layers" to "appConfigLayerArn,mysql-layer,crypto-layer,aggregate-v1-layer",
                Keys.LAMBDA_ROLE to monitorConfigProvider.getLambdaRole(),
                "name" to (workflowContext.sharedData["lambda-name"] ?: "defaultFuncion")
        ))
        addStep("EnableDynamoDbStream", "EnableDynamoDbStream")
        addStep("AddLambdaEventSource", "AddLambdaEventSource")

        steps.forEach {
            it.step.initialize()
        }

        return steps
    }

    override fun getRequiredParameters(): List<RequiredWorkflowParameter> {
        return listOf(RequiredWorkflowParameter(Keys.DATABASE_NAME, WorkflowParameterType.STRING),
                RequiredWorkflowParameter(Keys.SOURCE_DYNAMODB_ARN, WorkflowParameterType.STRING),
                RequiredWorkflowParameter("tableNames", WorkflowParameterType.STRING),
                RequiredWorkflowParameter("instancesCount", WorkflowParameterType.INT),
                RequiredWorkflowParameter("lambda-name", WorkflowParameterType.STRING),
                RequiredWorkflowParameter("dbInstanceName", WorkflowParameterType.STRING))
    }


    override val name: String = "AggregateSimpleMysqlDatabaseTemplate"
}