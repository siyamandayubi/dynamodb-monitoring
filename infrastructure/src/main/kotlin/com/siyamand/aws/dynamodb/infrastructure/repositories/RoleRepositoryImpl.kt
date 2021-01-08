package com.siyamand.aws.dynamodb.infrastructure.repositories

import com.siyamand.aws.dynamodb.core.entities.*
import kotlinx.coroutines.reactive.awaitFirst
import com.siyamand.aws.dynamodb.core.repositories.RoleRepository
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import com.siyamand.aws.dynamodb.infrastructure.mappers.CredentialMapper
import com.siyamand.aws.dynamodb.infrastructure.mappers.ResourceMapper
import com.siyamand.aws.dynamodb.infrastructure.mappers.RoleMapper
import reactor.core.publisher.Mono
import software.amazon.awssdk.services.iam.model.AttachRolePolicyRequest
import software.amazon.awssdk.services.iam.model.CreateInstanceProfileRequest
import software.amazon.awssdk.services.iam.model.CreatePolicyRequest
import software.amazon.awssdk.services.iam.model.CreateRoleRequest

class RoleRepositoryImpl(private val clientBuilder: ClientBuilder) : RoleRepository, AwsBaseRepositoryImpl() {
    override suspend fun getList(): List<RoleEntity> {
        val client = getClient(clientBuilder::buildAmazonIdentityManagementAsyncClient)
        val response = client.listRoles().thenApply { it.roles().map { RoleMapper.convert(it) } }
        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun addRole(createRoleEntity: CreateRoleEntity): ResourceEntity {
        val client = getClient(clientBuilder::buildAmazonIdentityManagementAsyncClient)
        val request = RoleMapper.convert(createRoleEntity)
        val returnValue = client.createRole(request).thenApply { ResourceMapper.convert(it.role().arn()) }
        return Mono.fromFuture(returnValue).awaitFirst()
    }

    override suspend fun addPolicy(createPolicyEntity: CreatePolicyEntity): ResourceEntity {
        val client = getClient(clientBuilder::buildAmazonIdentityManagementAsyncClient)
        val request = RoleMapper.convert(createPolicyEntity)
        val returnValue = client.createPolicy(request).thenApply { ResourceMapper.convert(it.policy().arn()) }
        return Mono.fromFuture(returnValue).awaitFirst()
    }

    override suspend fun attachRolePolicy(roleName: String, policyArn: String):Boolean {
        val client = getClient(clientBuilder::buildAmazonIdentityManagementAsyncClient)
        val response = client.attachRolePolicy(AttachRolePolicyRequest
                .builder()
                .roleName(roleName)
                .policyArn(policyArn)
                .build()).thenApply { true }

        return Mono.fromFuture(response).awaitFirst()
    }
}