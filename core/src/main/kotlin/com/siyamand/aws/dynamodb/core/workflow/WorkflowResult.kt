package com.siyamand.aws.dynamodb.core.workflow

class WorkflowResult(val resultType: WorkflowResultType, val params: Map<String, String>, val message: String) {
}

enum class WorkflowResultType {
    SUCCESS,
    ERROR,
    WAITING,
    FINISH
}