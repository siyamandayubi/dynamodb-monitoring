package com.siyamand.aws.dynamodb.core.rds

import com.siyamand.aws.dynamodb.core.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.database.DatabaseCredentialEntity
import com.siyamand.aws.dynamodb.core.role.RoleEntity
import com.siyamand.aws.dynamodb.core.secretManager.SecretEntity

interface RdsBuilder {
    fun build(name: String, credential: DatabaseCredentialEntity, credentialResourceEntity: ResourceEntity, metadataId: String): CreateDbInstanceEntity
    fun createProxyEntity(role: RoleEntity, subnets: List<String>, rds: RdsEntity, secretArn: String, metadataId: String): CreateProxyEntity
}