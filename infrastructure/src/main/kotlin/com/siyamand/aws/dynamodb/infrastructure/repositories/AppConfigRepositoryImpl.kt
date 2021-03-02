package com.siyamand.aws.dynamodb.infrastructure.repositories

import com.siyamand.aws.dynamodb.core.sdk.appconfig.*
import com.siyamand.aws.dynamodb.core.sdk.appconfig.entities.*
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import com.siyamand.aws.dynamodb.infrastructure.mappers.AppConfigMapper
import kotlinx.coroutines.reactive.awaitFirst
import reactor.core.publisher.Mono
import software.amazon.awssdk.services.appconfig.model.*

class AppConfigRepositoryImpl(private val clientBuilder: ClientBuilder) : AppConfigRepository, AwsBaseRepositoryImpl() {

    override suspend fun addApplication(entity: CreateApplicationEntity): ApplicationEntity {
        val client = getClient(clientBuilder::buildAppConfigAsyncClient)
        val response = client.createApplication(AppConfigMapper.convert(entity)).thenApply { AppConfigMapper.convert(it!!) }
        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun addEnvironment(entity: CreateEnvironmentEntity): EnvironmentEntity {
        val client = getClient(clientBuilder::buildAppConfigAsyncClient)
        val response = client.createEnvironment(AppConfigMapper.convert(entity)).thenApply { AppConfigMapper.convert(it!!) }
        return Mono.fromFuture(response).awaitFirst()
    }


    override suspend fun addDeploymentStrategy(entity: CreateDeploymentStrategyEntity): DeploymentStrategyEntity {
        val client = getClient(clientBuilder::buildAppConfigAsyncClient)
        val response = client.createDeploymentStrategy(AppConfigMapper.convert(entity)).thenApply(AppConfigMapper::convert)
        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun addConfigurationProfile(entity: CreateConfigurationProfileEntity): ConfigurationProfileEntity {
        val client = getClient(clientBuilder::buildAppConfigAsyncClient)
        val response = client.createConfigurationProfile(AppConfigMapper.convert(entity)).thenApply(AppConfigMapper::convert)
        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun addHostedConfigurationVersion(entity: CreateHostedConfigurationVersionEntity): String? {
        val client = getClient(clientBuilder::buildAppConfigAsyncClient)
        val response = client.createHostedConfigurationVersion(AppConfigMapper.convert(entity)).thenApply{it.versionNumber()?.toString()}
        return Mono.fromFuture(response).awaitFirst()
    }
}