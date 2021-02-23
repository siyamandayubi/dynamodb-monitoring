package com.siyamand.aws.dynamodb.core.role

interface PolicyBuilder {
    fun createLambdaPolicy(): CreatePolicyEntity
    fun createAccessRdsProxyPolicy(): CreatePolicyEntity
    fun createLambdaSecretManagerPolicy(): CreatePolicyEntity
    fun createLambdaEc2Policy(): CreatePolicyEntity
    fun createRdsProxyPolicy(): CreatePolicyEntity
}