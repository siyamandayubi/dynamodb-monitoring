package com.siyamand.aws.dynamodb.core.services

import com.siyamand.aws.dynamodb.core.builders.DatabaseCredentialBuilder
import com.siyamand.aws.dynamodb.core.builders.RdsBuilder
import com.siyamand.aws.dynamodb.core.builders.SecretBuilder
import com.siyamand.aws.dynamodb.core.entities.CreateSecretEntity
import com.siyamand.aws.dynamodb.core.entities.RdsListEntity
import com.siyamand.aws.dynamodb.core.entities.ResourceEntity
import com.siyamand.aws.dynamodb.core.entities.database.CreateProxyEntity
import com.siyamand.aws.dynamodb.core.entities.database.DatabaseConnectionEntity
import com.siyamand.aws.dynamodb.core.entities.database.DatabaseCredentialEntity
import com.siyamand.aws.dynamodb.core.entities.database.UserAuthConfigEntity
import com.siyamand.aws.dynamodb.core.repositories.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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

        val databaseCredential = databaseCredentialBuilder.build()
        val createSecretRequest = secretBuilder.buildCreateRequest(name, databaseCredential)

        var existingSecret = secretManagerRepository.getSecret(createSecretRequest.name)

        var counter = 0;
        while (existingSecret != null) {
            counter++;
            createSecretRequest.name = "${name}_$counter";
            existingSecret = secretManagerRepository.getSecret(createSecretRequest.name)
        }

        val credentialResource = secretManagerRepository.addSecret(createSecretRequest)
        val createRequest = rdsBuilder.build(name, databaseCredential, credentialResource)

        return rdsRepository.createDatabase(createRequest)
    }

    override suspend fun getList(marker: String): RdsListEntity {
        initialize()
        return rdsRepository.list(marker)
    }

    override suspend fun createDatabase(rdsIdentifier: String, secretName: String) {
        initialize()
        var existingSecret = secretManagerRepository.getSecret(secretName)
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
        val role = roleService.getOrCreateLambdaRole()
        var existingSecret = secretManagerRepository.getSecret(secretName)
        val rdsList = rdsRepository.getRds(rdsIdentifier)
        if (!rdsList.any()) {
            throw Exception("No Rds has been found")
        }
        val rds = rdsList.first()
        val vpcs = vpcRepository.getSecurityGroupVpcs(rds.VpcSecurityGroupMemberships.map { it.vpcSecurityGroupId })
        val subnets = vpcRepository.getSubnets(vpcs)
        val request = CreateProxyEntity()
        request.roleArn = role.resource.arn
        request.engineFamily = "MYSQL"
        request.dbProxyName = rdsIdentifier
        request.vpcSubnetIds = subnets
        request.vpcSecurityGroupIds = rds.VpcSecurityGroupMemberships.map { it.vpcSecurityGroupId }
        var auth = UserAuthConfigEntity()
        auth.secretArn = existingSecret!!.resourceEntity.arn
        request.auth.add(auth)
        return rdsRepository.createProxy(request)
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