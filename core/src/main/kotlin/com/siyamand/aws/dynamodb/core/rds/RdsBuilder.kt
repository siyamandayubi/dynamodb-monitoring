package com.siyamand.aws.dynamodb.core.rds

import com.siyamand.aws.dynamodb.core.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.database.DatabaseCredentialEntity

interface RdsBuilder {
    fun build(name: String, credential: DatabaseCredentialEntity, credentialResourceEntity: ResourceEntity): CreateDbInstanceEntity
}