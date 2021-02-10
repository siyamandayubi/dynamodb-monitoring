package com.siyamand.aws.dynamodb.web.models

import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.AggregateMonitoringEntity

class StartWorkflowModel(var sourceTableName: String = "", var workflowName :String = "") {
}