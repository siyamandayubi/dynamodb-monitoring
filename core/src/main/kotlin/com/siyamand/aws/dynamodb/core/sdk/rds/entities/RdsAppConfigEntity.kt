package com.siyamand.aws.dynamodb.core.sdk.rds.entities

import kotlinx.serialization.Serializable

@Serializable
class RdsAppConfigEntity{
    var endpoints: MutableList<RdsAppConfigItemEntity> = mutableListOf()
    var databaseName: String = ""
}

@Serializable
class RdsAppConfigItemEntity {
    var arn: String = ""
    var endPoint: String = ""
    var port: Int = 3306
    var start: String = "000"
    var end: String = "000"
    var order: Int = 0
}