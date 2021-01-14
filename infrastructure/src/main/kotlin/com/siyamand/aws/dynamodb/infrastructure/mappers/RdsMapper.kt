package com.siyamand.aws.dynamodb.infrastructure.mappers

import com.siyamand.aws.dynamodb.core.entities.database.CreateDbInstanceEntity
import com.siyamand.aws.dynamodb.core.entities.database.CreateProxyEntity
import com.siyamand.aws.dynamodb.core.entities.database.UserAuthConfigEntity
import software.amazon.awssdk.services.rds.model.CreateDbInstanceRequest
import software.amazon.awssdk.services.rds.model.CreateDbProxyRequest
import software.amazon.awssdk.services.rds.model.Tag
import software.amazon.awssdk.services.rds.model.UserAuthConfig

class RdsMapper {
    companion object {
        fun convert(entity: CreateProxyEntity): CreateDbProxyRequest {
            val builder = CreateDbProxyRequest
                    .builder()
                    .dbProxyName(entity.dbProxyName)
                    .roleArn(entity.roleArn)
                    .tags(entity.tags.map { Tag.builder().key(it.name).value(it.value).build() })
                    .engineFamily(entity.engineFamily)
                    .auth(entity.auth.map(RdsMapper::convert))

            return builder.build()
        }

        fun convert(entity: UserAuthConfigEntity): UserAuthConfig {
            val builder = UserAuthConfig
                    .builder()
                    .authScheme(entity.authScheme)
                    .description(entity.description)
                    .iamAuth(entity.iamAuth)
                    .secretArn(entity.secretArn)
                    .userName(entity.userName)

            return builder.build()
        }

        fun convert(entity: CreateDbInstanceEntity): CreateDbInstanceRequest {
            val builder = CreateDbInstanceRequest
                    .builder()
                    .dbName(entity.dbName)
                    .dbInstanceIdentifier(entity.instanceName)
                    .engine(entity.engine)
                    .engineVersion(entity.engineVersion)
                    .masterUsername(entity.masterUsername)
                    .masterUserPassword(entity.masterPassword)
                    .availabilityZone(entity.availabilityZone)
                    .tags(entity.tags.map { Tag.builder().key(it.name).value(it.value).build() })

            return builder.build()
        }
    }
}