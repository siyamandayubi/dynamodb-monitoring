package com.siyamand.aws.dynamodb.core.builders

import com.siyamand.aws.dynamodb.core.entities.CreatePolicyEntity

interface PolicyBuilder {
    fun createLambdaPolicy(): CreatePolicyEntity
    fun createRdsProxyPolicy(): CreatePolicyEntity
    fun createLambdaSecretManagerPolicy(): CreatePolicyEntity
    fun createLambdaEc2Policy(): CreatePolicyEntity
}