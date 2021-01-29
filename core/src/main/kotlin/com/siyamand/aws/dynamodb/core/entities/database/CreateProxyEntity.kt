package com.siyamand.aws.dynamodb.core.entities.database

import com.siyamand.aws.dynamodb.core.entities.TagEntity

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

class UserAuthConfigEntity {

    val description: String? = null

    val userName: String? = null

    val authScheme: String? = null

    var secretArn: String? = null

    val iamAuth: String? = null

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