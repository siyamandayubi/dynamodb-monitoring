package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.PrerequisteEntity
import com.siyamand.aws.dynamodb.core.sdk.authentication.CredentialEntity
import com.siyamand.aws.dynamodb.core.sdk.dynamodb.TableDetailEntity

interface PrerequisiteReadonlyService {
    suspend fun getPrerequistes(): PrerequisteEntity
    suspend fun getMonitoringTable(): TableDetailEntity?
}

interface PrerequisiteService : PrerequisiteReadonlyService {
    suspend fun createPrerequistes(credentialEntity: CredentialEntity): PrerequisteEntity
    suspend fun getOrCreateMonitoringTable(): TableDetailEntity
}