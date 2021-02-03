package com.siyamand.aws.dynamodb.infrastructure.mappers

import com.siyamand.aws.dynamodb.core.rds.*
import software.amazon.awssdk.services.rds.model.*

class RdsMapper {
    companion object {
        fun convert(entity: CreateProxyEntity): CreateDbProxyRequest {
            val builder = CreateDbProxyRequest
                    .builder()
                    .dbProxyName(entity.dbProxyName)
                    .roleArn(entity.roleArn)
                    .tags(entity.tags.map { Tag.builder().key(it.name).value(it.value).build() })
                    .engineFamily(entity.engineFamily)
                    .vpcSecurityGroupIds(entity.vpcSecurityGroupIds)
                    .vpcSubnetIds(entity.vpcSubnetIds)
                    .auth(entity.auth.map(RdsMapper::convert))

            return builder.build()
        }

        fun convert(entity: CreateDbProxyTargetEntity): RegisterDbProxyTargetsRequest {
            return RegisterDbProxyTargetsRequest
                    .builder()
                    .dbProxyName(entity.dbProxyName)
                    .targetGroupName(entity.targetGroupName)
                    .dbInstanceIdentifiers(entity.dbInstanceIdentifiers)
                    .build()
        }

        fun convert(target: DBProxyTarget): DbProxyTargetEntity {
            return DbProxyTargetEntity(
                    target.targetArn(),
                    target.endpoint(),
                    target.trackedClusterId(),
                    target.rdsResourceId(),
                    target.port(),
                    target.type().name
            )
        }

        fun convert(dbInstance: DBInstance): RdsEntity {
            return RdsEntity(
                    dbInstance.dbName(),
                    dbInstance.endpoint().address(),
                    dbInstance.dbInstanceArn(),
                    dbInstance.endpoint().port(),
                    dbInstance.masterUsername(),
                    dbInstance.vpcSecurityGroups().map { VpcSecurityGroupMembershipEntity(it.vpcSecurityGroupId(), it.status()) }.toMutableList()
            )
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
                    .dbInstanceClass(entity.dbInstanceClass)
                    .publiclyAccessible(entity.publiclyAccessible)
                    .masterUserPassword(entity.masterPassword)
                    .allocatedStorage(entity.allocatedStorage)
                    .tags(entity.tags.map { Tag.builder().key(it.name).value(it.value).build() })

            if (!entity.availabilityZone.isNullOrEmpty()) {
                builder.availabilityZone(entity.availabilityZone)
            }

            return builder.build()
        }
    }
}