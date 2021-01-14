package com.siyamand.aws.dynamodb.core.entities.database

import com.siyamand.aws.dynamodb.core.entities.TagEntity

class CreateProxyEntity {
    val dbProxyName: String? = null

    val engineFamily: String? = null

    val auth: List<UserAuthConfigEntity> = mutableListOf()

    val roleArn: String? = null

    val vpcSubnetIds: List<String>? = null

    val vpcSecurityGroupIds: List<String>? = null

    val requireTLS: Boolean? = null

    val idleClientTimeout: Int? = null

    val debugLogging: Boolean? = null

    val tags: List<TagEntity> = mutableListOf()
}

class UserAuthConfigEntity {

    val description: String? = null

    val userName: String? = null

    val authScheme: String? = null

    val secretArn: String? = null

    val iamAuth: String? = null

}