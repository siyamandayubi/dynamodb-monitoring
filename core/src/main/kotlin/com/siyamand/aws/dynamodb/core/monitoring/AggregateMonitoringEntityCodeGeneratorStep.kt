package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.AggregateMonitoringEntity
import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.MonitoringBaseEntity
import com.siyamand.aws.dynamodb.core.template.TemplateEngine
import com.siyamand.aws.dynamodb.core.workflow.*
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.reflect.KClass

class AggregateMonitoringEntityCodeGeneratorStep(private val templateEngine: TemplateEngine) : WorkflowStep() {
    override val name: String = "AggregateMonitoringEntityCodeGenerator"

    override suspend fun execute(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        if (!params.containsKey("code-path")) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "parameter 'code-path' missed")
        }

        val entity = genericCastOrNull<MonitoringBaseEntity<AggregateMonitoringEntity>>(owner)
                ?: return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "type mismatch: owner")

        val codePath = params["code-path"] ?: ""

        val uri = javaClass.classLoader.getResource(codePath).toURI()
        val template = Files.readString(Paths.get(uri))

        val templateEngineParams = mutableMapOf<String, Any>()
        templateEngineParams.putAll(params)
        templateEngineParams["entity"] = entity.relatedData
        templateEngineParams["workflow"] = instance
        val result = templateEngine.execute(template, templateEngineParams)

        instance.context.sharedData[Keys.CODE_RESULT] = result
        return WorkflowResult(WorkflowResultType.SUCCESS, mapOf(Keys.CODE_RESULT to result), "")
    }

    override suspend fun isWaiting(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        return execute(instance, owner, params)
    }

    override suspend fun initialize() {
    }

    private inline fun <reified T> genericCastOrNull(anything: Any): T? {
        return anything as? T
    }
}