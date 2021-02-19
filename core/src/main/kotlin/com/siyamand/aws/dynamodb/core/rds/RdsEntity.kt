package com.siyamand.aws.dynamodb.core.rds

import com.siyamand.aws.dynamodb.core.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.resource.TagEntity

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

class RdsListEntity(val marker: String?, val list: List<RdsEntity>)

class VpcSecurityGroupMembershipEntity(val vpcSecurityGroupId: String, val status: String)