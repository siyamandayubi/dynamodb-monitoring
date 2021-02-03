package com.siyamand.aws.dynamodb.core.rds

class RdsEntity(
        val instanceName: String,
        val endPoint: String,
        val arn: String,
        val port: Int,
        val masterUserName: String,
        val VpcSecurityGroupMemberships: MutableList<VpcSecurityGroupMembershipEntity> = mutableListOf<VpcSecurityGroupMembershipEntity>()) {

}

class RdsListEntity(val marker: String?, val list: List<RdsEntity>)

class VpcSecurityGroupMembershipEntity(val vpcSecurityGroupId: String, val status: String)