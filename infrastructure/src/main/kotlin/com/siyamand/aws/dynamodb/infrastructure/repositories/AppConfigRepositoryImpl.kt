package com.siyamand.aws.dynamodb.infrastructure.repositories

import com.siyamand.aws.dynamodb.core.common.PageResultEntity
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
        val response = client.createHostedConfigurationVersion(AppConfigMapper.convert(entity)).thenApply { it.versionNumber()?.toString() }
        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun startDeployment(entity: StartDeploymentEntity): DeploymentStatusEntity {
        val client = getClient(clientBuilder::buildAppConfigAsyncClient)
        val response = client.startDeployment(AppConfigMapper.convert(entity)).thenApply(AppConfigMapper::convert)
        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun getApplications(nextToken: String): PageResultEntity<ApplicationEntity> {
        val requestBuilder = ListApplicationsRequest.builder()
        if (!nextToken.isNullOrEmpty()) {
            requestBuilder.nextToken(nextToken)
        }
        val client = getClient(clientBuilder::buildAppConfigAsyncClient)
        val response = client.listApplications(requestBuilder.build()).thenApply {
            PageResultEntity(it.items().map(AppConfigMapper::convert), it.nextToken() ?: "")
        }

        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun getEnvironments(applicationId:String, nextToken: String): PageResultEntity<EnvironmentEntity> {
        val requestBuilder = ListEnvironmentsRequest.builder().applicationId(applicationId)
        if (!nextToken.isNullOrEmpty()) {
            requestBuilder.nextToken(nextToken)
        }
        val client = getClient(clientBuilder::buildAppConfigAsyncClient)
        val response = client.listEnvironments(requestBuilder.build()).thenApply {
            PageResultEntity(it.items().map(AppConfigMapper::convert), it.nextToken() ?: "")
        }

        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun getProfiles(applicationId:String, nextToken: String): PageResultEntity<ConfigurationProfileEntity> {
        val requestBuilder = ListConfigurationProfilesRequest.builder().applicationId(applicationId)
        if (!nextToken.isNullOrEmpty()) {
            requestBuilder.nextToken(nextToken)
        }
        val client = getClient(clientBuilder::buildAppConfigAsyncClient)
        val response = client.listConfigurationProfiles(requestBuilder.build()).thenApply {
            PageResultEntity(it.items().map(AppConfigMapper::convert), it.nextToken() ?: "")
        }

        return Mono.fromFuture(response).awaitFirst()
    }

    override suspend fun getDeploymentStrategies(nextToken: String): PageResultEntity<DeploymentStrategyEntity> {
        val requestBuilder = ListDeploymentStrategiesRequest.builder()
        if (!nextToken.isNullOrEmpty()) {
            requestBuilder.nextToken(nextToken)
        }
        val client = getClient(clientBuilder::buildAppConfigAsyncClient)
        val response = client.listDeploymentStrategies(requestBuilder.build()).thenApply {
            PageResultEntity(it.items().map(AppConfigMapper::convert), it.nextToken() ?: "")
        }

        return Mono.fromFuture(response).awaitFirst()
    }
    override suspend fun getDeployment(applicationId: String, environmentId: String, deploymentNumber: Int): DeploymentStatusEntity {
        val client = getClient(clientBuilder::buildAppConfigAsyncClient)
        val response = client.getDeployment(GetDeploymentRequest
                .builder()
                .applicationId(applicationId)
                .environmentId(environmentId)
                .deploymentNumber(deploymentNumber)
                .build())
                .thenApply(AppConfigMapper::convert)
        return Mono.fromFuture(response).awaitFirst()
    }
}