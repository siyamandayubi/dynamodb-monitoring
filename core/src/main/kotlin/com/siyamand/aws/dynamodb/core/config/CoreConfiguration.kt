package com.siyamand.aws.dynamodb.core.config

import com.siyamand.aws.dynamodb.core.builders.PolicyBuilder
import com.siyamand.aws.dynamodb.core.builders.PolicyBuilderImpl
import com.siyamand.aws.dynamodb.core.builders.RoleBuilder
import com.siyamand.aws.dynamodb.core.builders.RoleBuilderImpl
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
    open fun getMonitoringTableAggregate(
            monitorConfigProvider: MonitorConfigProvider,
            tableRepository: TableRepository,
            resourceRepository: ResourceRepository): MetadataService {
        return MetadataServiceImpl(resourceRepository, monitorConfigProvider, tableRepository)
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
    open fun getFunctionService(lambdaRepository: LambdaRepository, credentialProvider: CredentialProvider): FunctionService {
        return FunctionServiceImpl(lambdaRepository, credentialProvider)
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