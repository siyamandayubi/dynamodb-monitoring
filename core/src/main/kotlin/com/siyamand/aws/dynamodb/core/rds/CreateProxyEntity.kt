package com.siyamand.aws.dynamodb.core.rds

import com.siyamand.aws.dynamodb.core.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.resource.TagEntity
import java.time.Instant

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

    val tags: List<TagEntity> = mutableListOf()
}

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

class UserAuthConfigEntity {

    var description: String? = null

    var userName: String? = null

    var authScheme: String? = null

    var secretArn: String? = null

    var iamAuth: String? = null

}

class CreateDbProxyTargetEntity(
        val dbProxyName: String,
        val targetGroupName: String,
        val dbInstanceIdentifiers: List<String>
)

class DbProxyTargetEntity(
        val targetArn: String,
        val endpoint: String,
        val trackedClusterId: String,
        val rdsResourceId: String,
        val port: Int,
        val type: String?
)