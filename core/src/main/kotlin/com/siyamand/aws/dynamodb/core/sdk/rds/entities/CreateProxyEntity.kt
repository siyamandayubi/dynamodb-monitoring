package com.siyamand.aws.dynamodb.core.sdk.rds.entities

import com.siyamand.aws.dynamodb.core.sdk.resource.TagEntity

class CreateProxyEntity {
    var dbProxyName: String? = null

    var engineFamily: String? = null

    var auth: MutableList<UserAuthConfigEntity> = mutableListOf()

    var roleArn: String? = null

    var vpcSubnetIds: List<String>? = null

    var vpcSecurityGroupIds: List<String>? = null

    val requireTLS: Boolean? = null

    val idleClientTimeout: Int? = null

    val debugLogging: Boolean? = null

    val tags: MutableList<TagEntity> = mutableListOf()
}

