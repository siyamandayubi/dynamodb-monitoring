package com.siyamand.aws.dynamodb.core.workflow.templates

import com.siyamand.aws.dynamodb.core.workflow.WorkflowStep
import com.siyamand.aws.dynamodb.core.workflow.WorkflowTemplate

class AggregateSimpleMysqlDatabaseTemplate : WorkflowTemplate {
    override val name: String = "AggregateSimpleMysqlDatabaseTemplate"
    override val steps: List<WorkflowStep> get() {return  listOf()}
}