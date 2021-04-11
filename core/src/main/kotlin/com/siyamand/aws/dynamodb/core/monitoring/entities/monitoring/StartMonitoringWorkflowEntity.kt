package com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring

import kotlinx.serialization.Serializable

@Serializable
class StartMonitoringWorkflowEntity {
    var sourceTableName: String = ""
    var workflowName: String = ""
    val instanceClass: String = ""
    var lambdaName: String = ""
    var rdsInstanceNamePrefix: String = ""
    var instancesCount: Int = 2
    var definition: AggregateMonitoringEntity? = null
}