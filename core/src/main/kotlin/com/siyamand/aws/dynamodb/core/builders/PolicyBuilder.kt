package com.siyamand.aws.dynamodb.core.builders

import com.siyamand.aws.dynamodb.core.entities.CreatePolicyEntity

interface PolicyBuilder {
    fun createLambdaPolicy(): CreatePolicyEntity
    fun createRdsProxyPolicy(): CreatePolicyEntity
}