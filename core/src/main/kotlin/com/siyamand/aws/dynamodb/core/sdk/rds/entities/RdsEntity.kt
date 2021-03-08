package com.siyamand.aws.dynamodb.core.sdk.rds.entities

import com.siyamand.aws.dynamodb.core.sdk.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.sdk.resource.TagEntity

class RdsEntity(
        val instanceName: String,
        val endPoint: String,
        val port: Int,
        val masterUserName: String,
        val status: String,
        val resource: ResourceEntity,
        val VpcSecurityGroupMemberships: MutableList<VpcSecurityGroupMembershipEntity> = mutableListOf<VpcSecurityGroupMembershipEntity>(),
        val tags: MutableList<TagEntity> = mutableListOf()) {

}

