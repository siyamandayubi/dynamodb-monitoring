package com.siyamand.aws.dynamodb.core.sdk.rds.entities

import com.siyamand.aws.dynamodb.core.sdk.resource.ResourceEntity
import java.time.Instant

class RdsProxyEntity(
        val dbProxyName: String,
        val dbProxyResource: ResourceEntity,
        val status: String,
        val engineFamily: String,
        val roleArn: String,
        val endpoint: String? = null,
        val requireTLS: Boolean? = null,
        val idleClientTimeout: Int? = null,
        val createdDate: Instant? = null,
        val updatedDate: Instant? = null){


    val vpcSecurityGroupIds: MutableList<String> = mutableListOf()

    val vpcSubnetIds: MutableList<String> = mutableListOf()

    val auth: MutableList<UserAuthConfigEntity> = mutableListOf()
}