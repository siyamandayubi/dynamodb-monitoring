package com.siyamand.aws.dynamodb.infrastructure.config

import com.siyamand.aws.dynamodb.core.repositories.LambdaRepository
import com.siyamand.aws.dynamodb.core.repositories.RoleRepository
import com.siyamand.aws.dynamodb.core.repositories.TableRepository
import com.siyamand.aws.dynamodb.core.repositories.TokenRepository
import com.siyamand.aws.dynamodb.core.services.CredentialProvider
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilderImpl
import com.siyamand.aws.dynamodb.infrastructure.repositories.LambdaRepositoryImpl
import com.siyamand.aws.dynamodb.infrastructure.repositories.RoleRepositoryImpl
import com.siyamand.aws.dynamodb.infrastructure.repositories.TableRepositoryImpl
import com.siyamand.aws.dynamodb.infrastructure.repositories.TokenRepositoryImpl
import com.siyamand.aws.dynamodb.infrastructure.services.CredentialProviderImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan
open class InfrastructureConfiguration() {
    @Bean
    open fun getTableRepository(clientBuilder: ClientBuilder): TableRepository {
        return TableRepositoryImpl(clientBuilder)
    }

    @Bean
    open fun getCredentialProvider(): CredentialProvider {
        return CredentialProviderImpl()
    }

    @Bean
    internal open fun getClientBuilder(): ClientBuilder {
        return ClientBuilderImpl()
    }

    @Bean
    open fun getLambdaRepository(clientBuilder: ClientBuilder): LambdaRepository{
        return LambdaRepositoryImpl(clientBuilder)
    }

    @Bean
    open fun getTokenRepository(clientBuilder: ClientBuilder): TokenRepository{
        return TokenRepositoryImpl(clientBuilder)
    }

    @Bean
    open fun getRoleRepository(clientBuilder: ClientBuilder): RoleRepository{
        return RoleRepositoryImpl(clientBuilder)
    }
}