package com.siyamand.aws.dynamodb.core.sdk.rds

import com.siyamand.aws.dynamodb.core.sdk.resource.TagEntity

interface CreateDbInstanceEntity {
    val dbName: String
    val instanceName: String
    val masterUsername: String
    val masterPassword: String
    val engine: String
    val engineVersion: String
    val availabilityZone: String
    val dbInstanceClass: String
    val publiclyAccessible: Boolean
    val allocatedStorage: Int
    val tags: MutableList<TagEntity>
    val vpcSecurityGroupIds: MutableList<String>
}
