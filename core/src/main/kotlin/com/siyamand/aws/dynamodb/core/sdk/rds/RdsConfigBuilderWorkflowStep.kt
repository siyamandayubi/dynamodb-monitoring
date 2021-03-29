package com.siyamand.aws.dynamodb.core.sdk.rds

import com.siyamand.aws.dynamodb.core.common.initializeRepositories
import com.siyamand.aws.dynamodb.core.sdk.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.sdk.rds.entities.RdsProxyEntity
import com.siyamand.aws.dynamodb.core.workflow.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RdsConfigBuilderWorkflowStep(
        private var credentialProvider: CredentialProvider,
        private val rdsRepository: RdsRepository,
        private val rdsConfigBuilder: RdsConfigBuilder) : WorkflowStep() {
    override val name: String = "RdsConfigBuilder"

    override suspend fun execute(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        if (!params.containsKey("proxyList")) {
            return WorkflowResult(WorkflowResultType.ERROR, params, "proxyList parameter is not provided")
        }

        if (!instance.context.sharedData.containsKey(params["proxyList"]!!)) {
            return WorkflowResult(WorkflowResultType.ERROR, params, "proxyList parameter is not provided")
        }

        if (!params.containsKey("output")) {
            return WorkflowResult(WorkflowResultType.ERROR, params, "output parameter is not provided")
        }

        // check database name
        if (!params.containsKey(Keys.DATABASE_NAME)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "The '${Keys.DATABASE_NAME}' field is mandatory")
        }

        credentialProvider.initializeRepositories(rdsRepository)

        val proxyNames = instance.context.sharedData[params["proxyList"]!!]!!.split(',').map { it.trim() }

        val proxies = getProxies(proxyNames)
        val config = rdsConfigBuilder.create(proxies, params[Keys.DATABASE_NAME]!!)
        instance.context.sharedData[params["output"]!!] = Json.encodeToString(config)

        return WorkflowResult(WorkflowResultType.SUCCESS, mapOf(), "")
    }

    private suspend fun getProxies(proxyNames: List<String>): MutableList<RdsProxyEntity> {
        var proxies = mutableListOf<RdsProxyEntity>()
        var proxyPage = rdsRepository.listProxies("")
        proxies.addAll(proxyPage.items)
        while (!proxyPage.nextPageToken.isNullOrEmpty()) {
            proxyPage = rdsRepository.listProxies(proxyPage.nextPageToken!!)
            proxies.addAll(proxyPage.items)
        }

        proxies = proxies.filter { proxy: RdsProxyEntity -> proxyNames.any { d: String -> d == proxy.dbProxyName } }.toMutableList()
        return proxies
    }

    override suspend fun isWaiting(instance: WorkflowInstance, owner: Any, params: Map<String, String>): WorkflowResult {
        return execute(instance, owner, params)
    }

    override suspend fun initialize() {
        this.credentialProvider = credentialProvider.threadSafe()
    }
}