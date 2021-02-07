package com.siyamand.aws.dynamodb.core.workflow

import kotlinx.serialization.Serializable

@Serializable
class WorkflowContext(val sharedData: Map<String, String>) {
}