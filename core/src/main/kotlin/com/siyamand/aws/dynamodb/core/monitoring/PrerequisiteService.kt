package com.siyamand.aws.dynamodb.core.monitoring

import com.siyamand.aws.dynamodb.core.monitoring.entities.monitoring.PrerequisteEntity

interface PrerequisiteService {
    suspend fun getPrerequistes(): PrerequisteEntity
}