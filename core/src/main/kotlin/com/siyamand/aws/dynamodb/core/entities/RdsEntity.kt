package com.siyamand.aws.dynamodb.core.entities

class RdsEntity(
        val instanceName: String,
        val endPoint: String,
        val arn: String,
        val port: Int,
        val masterUserName: String) {
}

class RdsListEntity(val marker: String?, val list: List<RdsEntity>)