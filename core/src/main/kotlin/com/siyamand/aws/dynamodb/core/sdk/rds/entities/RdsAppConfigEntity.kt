package com.siyamand.aws.dynamodb.core.sdk.rds.entities

class RdsAppConfigEntity{
    var endpoints: MutableList<RdsAppConfigItemEntity> = mutableListOf()
    var databaseName: String = ""
}

class RdsAppConfigItemEntity {
    var arn: String = ""
    var endPoint: String = ""
    var port: Int = 3306
    var start: String = "000"
    var end: String = "000"
    var order: Int = 0
}