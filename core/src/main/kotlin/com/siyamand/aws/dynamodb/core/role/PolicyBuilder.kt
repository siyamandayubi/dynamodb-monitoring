package com.siyamand.aws.dynamodb.core.role

interface PolicyBuilder {
    fun createLambdaPolicy(): CreatePolicyEntity
    fun createRdsProxyPolicy(): CreatePolicyEntity
    fun createLambdaSecretManagerPolicy(): CreatePolicyEntity
    fun createLambdaEc2Policy(): CreatePolicyEntity
}