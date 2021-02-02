package com.siyamand.aws.dynamodb.core.config

import com.siyamand.aws.dynamodb.core.builders.*
import com.siyamand.aws.dynamodb.core.repositories.*
import com.siyamand.aws.dynamodb.core.services.*
import com.siyamand.aws.dynamodb.core.services.TableServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan
open class CoreConfiguration {

    @Bean
    open fun getVpcService(credentialProvider: CredentialProvider, vpcRepository: VpcRepository): VpcService{
        return  VpcServiceImpl(credentialProvider, vpcRepository)
    }
    @Bean
    open fun getMonitoringTableAggregate(
            monitorConfigProvider: MonitorConfigProvider,
            tableRepository: TableRepository,
            resourceRepository: ResourceRepository): MetadataService {
        return MetadataServiceImpl(resourceRepository, monitorConfigProvider, tableRepository)
    }

    @Bean
    open fun getS3Service(credentialProvider: CredentialProvider, monitorConfigProvider: MonitorConfigProvider, s3Repository: S3Repository): S3Service {
        return S3ServiceImpl(credentialProvider, s3Repository, monitorConfigProvider)
    }

    @Bean
    open fun getRdsService(
            roleService: RoleService,
            credentialProvider: CredentialProvider,
            rdsRepository: RdsRepository,
            vpcRepository: VpcRepository,
            secretBuilder: SecretBuilder,
            secretManagerRepository: SecretManagerRepository,
            databaseRepository: DatabaseRepository,
            resourceRepository: ResourceRepository,
            rdsBuilder: RdsBuilder,
            databaseCredentialBuilder: DatabaseCredentialBuilder): RdsService {
        return RdsServiceImpl(
                roleService,
                credentialProvider,
                rdsBuilder,
                secretBuilder,
                databaseCredentialBuilder,
                rdsRepository,
                resourceRepository,
                vpcRepository,
                databaseRepository,
                secretManagerRepository)
    }
    @Bean
    open fun getDatabaseService(monitorConfigProvider: MonitorConfigProvider, databaseRepository: DatabaseRepository): DatabaseService {
        return DatabaseServiceImpl(monitorConfigProvider, databaseRepository)
    }
    @Bean
    open fun getRdsBuilder(monitorConfigProvider: MonitorConfigProvider): RdsBuilder {
        return RdsBuilderImpl(monitorConfigProvider)
    }

    @Bean
    open fun getSecretBuilder(monitorConfigProvider: MonitorConfigProvider): SecretBuilder {
        return SecretBuilderImpl(monitorConfigProvider)
    }

    @Bean
    open fun getDatabaseCredentialBuilder(): DatabaseCredentialBuilder {
        return DatabaseCredentialBuilderImpl()
    }

    @Bean
    open fun getTableService(
            tableRepository: TableRepository,
            credentialProvider: CredentialProvider): TableService {
        return TableServiceImpl(tableRepository, credentialProvider)
    }

    @Bean
    open fun getAuthenticationService(tokenRepository: TokenRepository): AuthenticationService {
        return AuthenticationServiceImpl(tokenRepository)
    }

    @Bean
    open fun getFunctionBuilder(): FunctionBuilder {
        return FunctionBuilderImpl()
    }

    @Bean
    open fun getFunctionService(
            monitorConfigProvider: MonitorConfigProvider,
            roleService: RoleService,
            functionBuilder: FunctionBuilder,
            lambdaRepository: LambdaRepository,
            credentialProvider: CredentialProvider): FunctionService {
        return FunctionServiceImpl(monitorConfigProvider, functionBuilder, roleService, lambdaRepository, credentialProvider)
    }

    @Bean
    open fun getPolicyBuilder(): PolicyBuilder {
        return PolicyBuilderImpl()
    }

    @Bean
    open fun getRoleBuilder(monitorConfigProvider: MonitorConfigProvider): RoleBuilder {
        return RoleBuilderImpl(monitorConfigProvider)
    }

    @Bean
    open fun getRoleService(
            roleRepository: RoleRepository,
            resourceRepository: ResourceRepository,
            credentialProvider: CredentialProvider,
            monitorConfigProvider: MonitorConfigProvider,
            policyBuilder: PolicyBuilder,
            roleBuilder: RoleBuilder): RoleService {
        return RoleServiceImpl(roleRepository, resourceRepository, credentialProvider, monitorConfigProvider, policyBuilder, roleBuilder)
    }
}