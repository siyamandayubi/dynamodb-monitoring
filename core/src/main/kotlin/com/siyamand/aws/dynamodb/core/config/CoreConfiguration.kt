package com.siyamand.aws.dynamodb.core.config

import com.siyamand.aws.dynamodb.core.repositories.LambdaRepository
import com.siyamand.aws.dynamodb.core.repositories.RoleRepository
import com.siyamand.aws.dynamodb.core.repositories.TableRepository
import com.siyamand.aws.dynamodb.core.repositories.TokenRepository
import com.siyamand.aws.dynamodb.core.services.*
import com.siyamand.aws.dynamodb.core.services.TableServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan
open class CoreConfiguration {
    @Bean
    open fun getTableService(
            tableRepository: TableRepository,
            credentialProvider: CredentialProvider): TableService {
        return TableServiceImpl(tableRepository, credentialProvider)
    }

    @Bean
    open fun getAuthenticationService(): AuthenticationService {
        return AuthenticationServiceImpl()
    }

    @Bean
    open fun getFunctionService(lambdaRepository: LambdaRepository, credentialProvider: CredentialProvider): FunctionService {
        return FunctionServiceImpl(lambdaRepository, credentialProvider)
    }

    @Bean
    open fun getRoleService(roleRepository: RoleRepository, credentialProvider: CredentialProvider): RoleService {
        return RoleServiceImpl(roleRepository, credentialProvider)
    }
}