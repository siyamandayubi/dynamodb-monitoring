package com.siyamand.aws.dynamodb.infrastructure.repositories

import com.siyamand.aws.dynamodb.core.common.NotExistException
import com.siyamand.aws.dynamodb.core.sdk.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.sdk.role.CreatePolicyEntity
import com.siyamand.aws.dynamodb.core.sdk.role.CreateRoleEntity
import com.siyamand.aws.dynamodb.core.sdk.role.RoleEntity
import kotlinx.coroutines.reactive.awaitFirst
import com.siyamand.aws.dynamodb.core.sdk.role.RoleRepository
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import com.siyamand.aws.dynamodb.infrastructure.mappers.ResourceMapper
import com.siyamand.aws.dynamodb.infrastructure.mappers.RoleMapper
import reactor.core.publisher.Mono
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain
import software.amazon.awssdk.services.iam.model.*
import java.util.concurrent.TimeUnit

class RoleRepositoryImpl(private val clientBuilder: ClientBuilder) : RoleRepository, AwsBaseRepositoryImpl() {
    override suspend fun getRoles(): List<RoleEntity> {
        val client = getClient(clientBuilder::buildAmazonIdentityManagementAsyncClient)
        val response = client.listRoles().thenApply { role -> role.roles().map { RoleMapper.convert(it) } }
        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun getRole(roleName: String): RoleEntity {
        val client = getClient(clientBuilder::buildAmazonIdentityManagementAsyncClient)
        val response = client.getRole(GetRoleRequest.builder().roleName(roleName).build()).thenApply { RoleMapper.convert(it.role()) }.orTimeout(1, TimeUnit.MINUTES)

        val errorHandler: (Throwable) -> Throwable = fun(exp: Throwable): Throwable {
            return if (exp is NoSuchEntityException) NotExistException(exp)
            else exp
        }
        DefaultAwsRegionProviderChain.builder().build().region
        return Mono.fromFuture(response).onErrorMap(errorHandler).awaitFirst()
    }

    override suspend fun getPolicies(path: String): List<ResourceEntity> {
        val client = getClient(clientBuilder::buildAmazonIdentityManagementAsyncClient)
        val list = client.listPolicies(ListPoliciesRequest.builder().pathPrefix(path).build())
                .thenApply { it.policies() }
                .thenApply { it.map { policy -> ResourceMapper.convert(policy.arn()) } }

        return Mono.fromFuture(list).awaitFirst()
    }

    override suspend fun getRolePolicies(roleName: String): List<String> {
        val client = getClient(clientBuilder::buildAmazonIdentityManagementAsyncClient)
        val response = client.listRolePolicies(ListRolePoliciesRequest.builder().roleName(roleName).build()).thenApply { it.policyNames() }
        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun addRole(createRoleEntity: CreateRoleEntity): RoleEntity {
        val client = getClient(clientBuilder::buildAmazonIdentityManagementAsyncClient)
        val request = RoleMapper.convert(createRoleEntity)
        val returnValue = client.createRole(request).thenApply { RoleMapper.convert(it.role()) }
        return Mono.fromFuture(returnValue).awaitFirst()
    }

    override suspend fun addPolicy(createPolicyEntity: CreatePolicyEntity): ResourceEntity {
        val client = getClient(clientBuilder::buildAmazonIdentityManagementAsyncClient)
        val request = RoleMapper.convert(createPolicyEntity)
        val returnValue = client.createPolicy(request).thenApply { ResourceMapper.convert(it.policy().arn()) }
        return Mono.fromFuture(returnValue).awaitFirst()
    }

    override suspend fun attachRolePolicy(roleName: String, policyArn: String): Boolean {
        val client = getClient(clientBuilder::buildAmazonIdentityManagementAsyncClient)
        val response = client.attachRolePolicy(AttachRolePolicyRequest
                .builder()
                .roleName(roleName)
                .policyArn(policyArn)
                .build()).thenApply { true }

        return Mono.fromFuture(response).awaitFirst()
    }
}