package com.siyamand.aws.dynamodb.core.rds

import com.siyamand.aws.dynamodb.core.database.DatabaseCredentialEntity
import com.siyamand.aws.dynamodb.core.resource.ResourceEntity
import com.siyamand.aws.dynamodb.core.resource.ResourceType
import com.siyamand.aws.dynamodb.core.resource.TagEntity
import com.siyamand.aws.dynamodb.core.common.MonitorConfigProvider


class RdsBuilderImpl(private val monitorConfigProvider: MonitorConfigProvider) : RdsBuilder {
    private companion object {
        const val USER_NAME = "root"
        const val ENGINE = "mysql"
        const val ENGINE_VERSION = "5.7.31"
        const val INSTANCE_CLASS = "db.t2.micro"
    }

    override fun build(name: String, credential: DatabaseCredentialEntity, credentialResourceEntity: ResourceEntity): CreateDbInstanceEntity {
        return CreateDbInstanceEntityImpl(
                name,
                name,
                credential.userName,
                credential.password,
                ENGINE,
                ENGINE_VERSION,
                "",
                INSTANCE_CLASS,
                true,
                20,
                mutableListOf(
                        TagEntity(monitorConfigProvider.getMonitoringGeneralTagName(), ResourceType.RDS.value),
                        TagEntity(monitorConfigProvider.getAccessTagName(), credentialResourceEntity.arn)))
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
            override val tags: MutableList<TagEntity> = mutableListOf()) : CreateDbInstanceEntity {
    }
}