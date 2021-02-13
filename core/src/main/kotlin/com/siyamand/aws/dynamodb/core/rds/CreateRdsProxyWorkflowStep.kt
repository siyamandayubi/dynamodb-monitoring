package com.siyamand.aws.dynamodb.core.rds

import com.siyamand.aws.dynamodb.core.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.common.initializeRepositories
import com.siyamand.aws.dynamodb.core.network.VpcRepository
import com.siyamand.aws.dynamodb.core.resource.ResourceRepository
import com.siyamand.aws.dynamodb.core.role.RoleService
import com.siyamand.aws.dynamodb.core.workflow.*

class CreateRdsProxyWorkflowStep(private val roleService: RoleService,
                                 private var credentialProvider: CredentialProvider,
                                 private val rdsRepository: RdsRepository,
                                 private val vpcRepository: VpcRepository,
                                 private val rdsBuilder: RdsBuilder,
                                 private val resourceRepository: ResourceRepository) : WorkflowStep() {
    override val name: String
        get() = "CreateRdsProxy"

    override suspend fun execute(context: WorkflowContext, params: Map<String, String>): WorkflowResult {
        if (!context.sharedData.containsKey(Keys.SECRET_ARN_KEY)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "No Secret key ${Keys.SECRET_ARN_KEY} found")
        }

        // check RDS key
        if (!context.sharedData.containsKey(Keys.RDS_ARN_KEY)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "No RDS ARN '${Keys.RDS_ARN_KEY}' exists in shared data")
        }

        // check database name
        if (!context.sharedData.containsKey(Keys.DATABASE_NAME)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "The '${Keys.DATABASE_NAME}' field is mandatory")
        }

        credentialProvider.initializeRepositories(resourceRepository, rdsRepository, vpcRepository)

        val rdsResource = resourceRepository.convert(context.sharedData[Keys.RDS_ARN_KEY]!!)
        val rdsList = rdsRepository.getRds(rdsResource.service)
        if (!rdsList.any()) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "No Rds has been found with the name '${rdsResource.service}'")
        }
        val rds = rdsList.first()
        val role = roleService.getOrCreateLambdaRole()

        val vpcs = vpcRepository.getSecurityGroupVpcs(rds.VpcSecurityGroupMemberships.map { it.vpcSecurityGroupId })
        val subnets = vpcRepository.getSubnets(vpcs)
        val request = rdsBuilder.createProxyEntity(role, rdsResource.service, subnets, rds, context.sharedData[Keys.SECRET_ARN_KEY]!!)
        val proxy = rdsRepository.createProxy(request)
        context.sharedData[Keys.PROXY_ARN_KEY] = proxy.dbProxyResource.arn
        if (proxy.status == "creating") {
            return WorkflowResult(WorkflowResultType.WAITING, mapOf(Keys.PROXY_ARN_KEY to proxy.dbProxyResource.arn), "")
        }

        return WorkflowResult(WorkflowResultType.SUCCESS, mapOf(Keys.PROXY_ARN_KEY to proxy.dbProxyResource.arn), "")
    }

    override suspend fun isWaiting(context: WorkflowContext, params: Map<String, String>): WorkflowResult {
        if (!context.sharedData.containsKey(Keys.PROXY_ARN_KEY)) {
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "No Proxy ARN ${Keys.PROXY_ARN_KEY} found")
        }

        credentialProvider.initializeRepositories(resourceRepository, rdsRepository, vpcRepository)

        val proxyResource = resourceRepository.convert(context.sharedData[Keys.PROXY_ARN_KEY]!!)
        val proxies = rdsRepository.getProxies(proxyResource.service)
        if(!proxies.items.any()){
            return WorkflowResult(WorkflowResultType.ERROR, mapOf(), "No Proxy has been found: ${proxyResource.arn}")
        }
        val proxy = proxies.items.first()
        if (proxy.status == "creating"){
            return WorkflowResult(WorkflowResultType.WAITING, mapOf(Keys.PROXY_ARN_KEY to proxy.dbProxyResource.arn), "")
        }

        return WorkflowResult(WorkflowResultType.SUCCESS, mapOf(Keys.PROXY_ARN_KEY to proxy.dbProxyResource.arn), "")
    }

    override suspend fun initialize() {
        this.credentialProvider = credentialProvider.threadSafe()
    }
}