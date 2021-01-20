package com.siyamand.aws.dynamodb.core.builders

import com.siyamand.aws.dynamodb.core.entities.ResourceEntity
import com.siyamand.aws.dynamodb.core.entities.database.CreateDbInstanceEntity
import com.siyamand.aws.dynamodb.core.entities.database.DatabaseCredentialEntity

interface RdsBuilder {
    fun build(name: String, credential:DatabaseCredentialEntity, credentialResourceEntity: ResourceEntity): CreateDbInstanceEntity
}