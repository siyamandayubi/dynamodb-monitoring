package com.siyamand.aws.dynamodb.core.sdk.rds.entities

class CreateDbProxyTargetEntity(
        val dbProxyName: String,
        val targetGroupName: String,
        val dbInstanceIdentifiers: List<String>
)