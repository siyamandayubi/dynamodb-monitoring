package com.siyamand.aws.dynamodb.infrastructure.repositories

import kotlinx.coroutines.reactive.awaitFirst
import com.siyamand.aws.dynamodb.core.sdk.authentication.TokenCredentialEntity
import com.siyamand.aws.dynamodb.core.sdk.authentication.TokenRepository
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import reactor.core.publisher.Mono
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain
import software.amazon.awssdk.services.sts.model.GetSessionTokenRequest
import java.util.*


class TokenRepositoryImpl(private val clientBuilder: ClientBuilder) : TokenRepository {
    private var region: String = "us-east-2"
    override suspend fun getAccessToken(keyId: String, secretAcessId: String): TokenCredentialEntity {
        val client = clientBuilder.buildSecurityTokenAsync(this.region, keyId, secretAcessId)

        val request = GetSessionTokenRequest.builder().durationSeconds(7200).build()
        val result = client.getSessionToken(request).thenApply {
            val credential = it.credentials()
            TokenCredentialEntity(
                    credential.accessKeyId(),
                    credential.secretAccessKey(),
                    credential.sessionToken(),
                    Date.from(credential.expiration()))
        }
        return Mono.fromFuture(result).awaitFirst()
    }

    override fun withRegion(region: String): TokenRepository {
        this.region = region
        return this
    }
}