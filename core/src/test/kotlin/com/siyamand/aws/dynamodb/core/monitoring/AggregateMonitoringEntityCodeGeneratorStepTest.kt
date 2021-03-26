package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.*
import com.siyamand.aws.dynamodb.core.template.TemplateEngineImpl
import com.siyamand.aws.dynamodb.core.workflow.Keys
import com.siyamand.aws.dynamodb.core.workflow.WorkflowContext
import com.siyamand.aws.dynamodb.core.workflow.WorkflowInstance
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant

@ExtendWith(SpringExtension::class)
internal class AggregateMonitoringEntityCodeGeneratorStepTest {

    @Test
    fun test_execute_generate_code_from_template() {
        runBlocking {
            val classUnderTest = AggregateMonitoringEntityCodeGeneratorStep(TemplateEngineImpl())

            val params = mapOf("code-path" to "lambdaTemplates/aggregateTemplate.ftl")
            val workflowInstance = WorkflowInstance("id", WorkflowContext(), listOf(), 0, null)
            val owner = MonitoringBaseEntity<AggregateMonitoringEntity>("id", "sourceTable", "type", MonitorStatus.INITIAL, 1, "workflow", AggregateMonitoringEntity())
            val groupBy = GroupByEntity()
            groupBy.fieldName = "group1"
            groupBy.tableName = "table1"
            val fieldDef = AggregateFieldEntity()
            fieldDef.name = "field1"
            fieldDef.path = "field2"
            fieldDef.from = Instant.now()
            groupBy.fields.add(fieldDef)
            owner.relatedData.groups.add(groupBy)
            val result = classUnderTest.execute(workflowInstance, owner, params)
            val code = workflowInstance.context.sharedData[Keys.CODE_RESULT]
            assertNotNull(code)
        }
    }
}