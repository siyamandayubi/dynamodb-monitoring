package com.siyamand.aws.dynamodb.web.models

import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.AggregateMonitoringEntity

class StartWorkflowModel{
        var sourceTableName: String = ""
        var workflowName: String = ""
        var instanceCount: Int = 1
        var instanceSize: String = ""
        var definition: AggregateMonitoringEntity? = null
}

