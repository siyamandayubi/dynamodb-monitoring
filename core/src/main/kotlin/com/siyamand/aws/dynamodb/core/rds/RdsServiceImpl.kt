package com.siyamand.aws.dynamodb.core.rds

import com.siyamand.aws.dynamodb.core.database.DatabaseConnectionEntity
import com.siyamand.aws.dynamodb.core.database.DatabaseCredentialBuilder
import com.siyamand.aws.dynamodb.core.database.DatabaseCredentialEntity
import com.siyamand.aws.dynamodb.core.secretManager.SecretBuilder
import com.siyamand.aws.dynamodb.core.database.DatabaseRepository
import com.siyamand.aws.dynamodb.core.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.network.VpcRepository
import com.siyamand.aws.dynamodb.core.authentication.CredentialProvider
import com.siyamand.aws.dynamodb.core.resource.ResourceRepository
import com.siyamand.aws.dynamodb.core.role.RoleService
import com.siyamand.aws.dynamodb.core.secretManager.SecretManagerRepository
import kotlinx.serialization.json.Json
import java.util.*

class RdsServiceImpl(
        private val roleService: RoleService,
        private val credentialProvider: CredentialProvider,
        private val rdsBuilder: RdsBuilder,
        private val secretBuilder: SecretBuilder,
        private val databaseCredentialBuilder: DatabaseCredentialBuilder,
        private val rdsRepository: RdsRepository,
        private val resourceRepository: ResourceRepository,
        private val vpcRepository: VpcRepository,
        private val databaseRepository: DatabaseRepository,
        private val secretManagerRepository: SecretManagerRepository) : RdsService {

    override suspend fun createDbInstance(name: String): ResourceEntity {
        initialize()

        val metadataId = UUID.randomUUID().toString()
        val databaseCredential = databaseCredentialBuilder.build()
        val createSecretRequest = secretBuilder.buildCreateRequest(name, databaseCredential, metadataId)

        var existingSecret = secretManagerRepository.getSecretValue(createSecretRequest.name)

        var counter = 0;
        while (existingSecret != null) {
            counter++;
            createSecretRequest.name = "${name}_$counter";
            existingSecret = secretManagerRepository.getSecretValue(createSecretRequest.name)
        }

        val credentialResource = secretManagerRepository.addSecret(createSecretRequest)
        val createRequest = rdsBuilder.build(name, databaseCredential, credentialResource, metadataId)

        return rdsRepository.createRds(createRequest).resource
    }

    override suspend fun getList(marker: String): RdsListEntity {
        initialize()
        return rdsRepository.list(marker)
    }

    override suspend fun createDatabase(rdsIdentifier: String, secretName: String) {
        initialize()
        var existingSecret = secretManagerRepository.getSecretValue(secretName)
        val credential = Json.decodeFromString(DatabaseCredentialEntity.serializer(), existingSecret!!.secretData)
        val rdsList = rdsRepository.getRds(rdsIdentifier)
        if (!rdsList.any()) {
            throw Exception("No Rds has been found")
        }
        val rds = rdsList.first()
        val databaseConnectionEntity = DatabaseConnectionEntity(credential,rds.endPoint, "test", rds.port)
        databaseRepository.createDatabase(databaseConnectionEntity)
    }

    override suspend fun createProxy(rdsIdentifier: String, secretName: String): ResourceEntity {
        initialize()
        val role = roleService.getOrCreateLambdaRole(null)
        var existingSecret = secretManagerRepository.getSecretValue(secretName)
        val rdsList = rdsRepository.getRds(rdsIdentifier)
        if (!rdsList.any()) {
            throw Exception("No Rds has been found")
        }
        val rds = rdsList.first()
        val vpcs = vpcRepository.getSecurityGroupVpcs(rds.VpcSecurityGroupMemberships.map { it.vpcSecurityGroupId })
        val subnets = vpcRepository.getSubnets(vpcs)
        val request = rdsBuilder.createProxyEntity(role, subnets, rds, existingSecret!!.resourceEntity.arn, UUID.randomUUID().toString())
        return rdsRepository.createProxy(request).dbProxyResource
    }

    private suspend fun initialize() {
        val credential = credentialProvider.getCredential()
                ?: throw SecurityException("No Credential has been provided");

        rdsRepository.initialize(credential, credentialProvider.getRegion());
        secretManagerRepository.initialize(credential, credentialProvider.getRegion())
        resourceRepository.initialize(credential, credentialProvider.getRegion())
        vpcRepository.initialize(credential, credentialProvider.getRegion())
    }
}