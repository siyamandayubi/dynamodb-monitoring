package com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring

import kotlinx.serialization.Serializable

@Serializable
class StartMonitoringWorkflowEntity {
    var sourceTableName: String = ""
    var workflowName: String = ""
    val instanceClass: String = ""
    var lambdaName: String = ""
    var instanceName: String = ""
    var definition: AggregateMonitoringEntity? = null
}