package com.siyamand.aws.dynamodb.core.sdk.rds

import com.siyamand.aws.dynamodb.core.sdk.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.database.DatabaseCredentialEntity
import com.siyamand.aws.dynamodb.core.sdk.rds.entities.CreateDbInstanceEntity
import com.siyamand.aws.dynamodb.core.sdk.rds.entities.CreateProxyEntity
import com.siyamand.aws.dynamodb.core.sdk.rds.entities.RdsEntity
import com.siyamand.aws.dynamodb.core.sdk.role.RoleEntity

interface RdsBuilder {
    fun build(name: String, credential: DatabaseCredentialEntity, credentialResourceEntity: ResourceEntity, metadataId: String): CreateDbInstanceEntity
    fun createProxyEntity(role: RoleEntity, subnets: List<String>, rds: RdsEntity, secretArn: String, metadataId: String): CreateProxyEntity
}