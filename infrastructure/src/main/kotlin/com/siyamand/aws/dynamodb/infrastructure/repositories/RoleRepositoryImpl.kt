package com.siyamand.aws.dynamodb.infrastructure.repositories

import kotlinx.coroutines.reactive.awaitFirst
import com.siyamand.aws.dynamodb.core.entities.CredentialEntity
import com.siyamand.aws.dynamodb.core.entities.RoleEntity
import com.siyamand.aws.dynamodb.core.repositories.RoleRepository
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import com.siyamand.aws.dynamodb.infrastructure.mappers.CredentialMapper
import com.siyamand.aws.dynamodb.infrastructure.mappers.RoleMapper
import reactor.core.publisher.Mono

class RoleRepositoryImpl(private val clientBuilder: ClientBuilder) : RoleRepository {
    private var token: CredentialEntity? = null
    private var region: String = "us-east-2";
    override suspend fun getList(): List<RoleEntity> {
        if (this.token == null) {
            throw IllegalArgumentException("token is not provider")
        }

        if (this.region.isNullOrEmpty()) {
            throw java.lang.IllegalArgumentException("region is not provider")
        }

        val credential = CredentialMapper.convert(this.token!!)
        val client = clientBuilder.buildAmazonIdentityManagementAsyncClient(this.region, credential)
        val response = client.listRoles().thenApply { it.roles().map { RoleMapper.convert(it) } }
        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun add(t: RoleEntity) {
        TODO("Not yet implemented")
    }

    override fun edit(t: RoleEntity) {
        TODO("Not yet implemented")
    }

    override fun delete(t: RoleEntity) {
        TODO("Not yet implemented")
    }

    override fun withToken(token: CredentialEntity): RoleRepository {
        this.token = token
        return this
    }

    override fun withRegion(region: String): RoleRepository {
        this.region = region
        return this
    }
}