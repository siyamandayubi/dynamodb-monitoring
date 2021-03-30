package com.siyamand.aws.dynamodb.core.sdk.rds

import com.siyamand.aws.dynamodb.core.database.DatabaseCredentialEntity
import com.siyamand.aws.dynamodb.core.sdk.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.sdk.resource.TagEntity
import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider
import com.siyamand.aws.dynamodb.core.sdk.rds.entities.CreateDbInstanceEntity
import com.siyamand.aws.dynamodb.core.sdk.rds.entities.*
import com.siyamand.aws.dynamodb.core.sdk.role.RoleEntity


class RdsBuilderImpl(private val monitorConfigProvider: MonitorConfigProvider) : RdsBuilder {
    private companion object {
        const val ENGINE = "mysql"
        const val ENGINE_VERSION = "5.7.31"
        const val INSTANCE_CLASS = "db.t2.micro"
    }

    override fun build(instanceName: String,
                       databaseName: String,
                       credential: DatabaseCredentialEntity,
                       credentialResourceEntity: ResourceEntity,
                       metadataId: String,
                       instanceClass: String?): CreateDbInstanceEntity {
        return CreateDbInstanceEntityImpl(
                databaseName,
                instanceName,
                credential.username,
                credential.password,
                ENGINE,
                ENGINE_VERSION,
                "",
                if (!instanceClass.isNullOrEmpty()) instanceClass else INSTANCE_CLASS,
                true,
                20,
                mutableListOf(
                        TagEntity(monitorConfigProvider.getMonitoringMetadataIdTagName(), metadataId),
                        TagEntity(monitorConfigProvider.getMonitoringVersionTagName(), monitorConfigProvider.getMonitoringVersionValue()),
                        TagEntity(monitorConfigProvider.getAccessTagName(), credentialResourceEntity.arn)))
    }

    override fun createProxyEntity(role: RoleEntity, subnets: List<String>, rds: RdsEntity, secretArn: String, metadataId: String): CreateProxyEntity {
        val request = CreateProxyEntity()
        request.roleArn = role.resource.arn
        request.engineFamily = "MYSQL"
        request.dbProxyName = rds.instanceName
        request.vpcSubnetIds = subnets
        request.vpcSecurityGroupIds = rds.VpcSecurityGroupMemberships.map { it.vpcSecurityGroupId }
        val auth = UserAuthConfigEntity()
        auth.secretArn = secretArn
        request.auth.add(auth)
        request.tags.add(TagEntity(monitorConfigProvider.getMonitoringMetadataIdTagName(), metadataId))
        request.tags.add(TagEntity(monitorConfigProvider.getMonitoringVersionTagName(), monitorConfigProvider.getMonitoringVersionValue()))
        return request
    }

    private class CreateDbInstanceEntityImpl(
            override val dbName: String,
            override val instanceName: String,
            override val masterUsername: String,
            override val masterPassword: String,
            override val engine: String,
            override val engineVersion: String,
            override val availabilityZone: String,
            override val dbInstanceClass: String,
            override val publiclyAccessible: Boolean,
            override val allocatedStorage: Int,
            override val tags: MutableList<TagEntity> = mutableListOf(),
            override val vpcSecurityGroupIds: MutableList<String> = mutableListOf()) : CreateDbInstanceEntity
}