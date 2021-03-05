package com.siyamand.aws.dynamodb.core.sdk.role

interface PolicyBuilder {
    fun createLambdaPolicy(): CreatePolicyEntity
    fun createAccessRdsProxyPolicy(): CreatePolicyEntity
    fun createLambdaSecretManagerPolicy(): CreatePolicyEntity
    fun createLambdaEc2Policy(): CreatePolicyEntity
    fun createRdsProxyPolicy(): CreatePolicyEntity
    fun createAppConfigAccessPolicy(): CreatePolicyEntity
    fun createS3AccessPolicy(): CreatePolicyEntity
}