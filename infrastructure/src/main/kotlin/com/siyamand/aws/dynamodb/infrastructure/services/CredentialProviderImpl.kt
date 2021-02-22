package com.siyamand.aws.dynamodb.infrastructure.services

import com.siyamand.aws.dynamodb.core.authentication.BasicCredentialEntity
import com.siyamand.aws.dynamodb.core.authentication.CredentialEntity
import com.siyamand.aws.dynamodb.core.authentication.TokenCredentialEntity
import com.siyamand.aws.dynamodb.core.authentication.CredentialProvider
import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.security.core.context.ReactiveSecurityContextHolder

class CredentialProviderImpl : CredentialProvider {

    @Autowired
    private val env: Environment? = null

    /*   private val currentRequest: HttpServletRequest?
           get() = (RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes)?.request
   */
    override suspend fun getCredential(): CredentialEntity? {
        val context = ReactiveSecurityContextHolder.getContext().awaitFirst()
        var principal = context.authentication.principal as TokenCredentialEntity?
        if (principal != null) {
            //  return principal
        }

        val awsAccessKey: String? = env?.getProperty("aws_access_key_id");
        val awsSecretKey: String? = env?.getProperty("aws_secret_access_key");

        if (awsAccessKey != null && awsSecretKey != null) {
            return BasicCredentialEntity(awsAccessKey, awsSecretKey, null)
        }

        return principal
    }

    override fun getRegion(): String {
        return "us-east-2"
    }

    override fun getGlobalRegion(): String {
        return "aws-global"
    }

    override suspend fun threadSafe(): CredentialProvider {
        return ThreadSafeCredentialProviderImpl(getRegion(), getGlobalRegion(), getCredential())
    }

    class ThreadSafeCredentialProviderImpl(private val region: String, private val globalRegion: String, private val credentialEntity: CredentialEntity?) : CredentialProvider {
        override suspend fun getCredential(): CredentialEntity? {
            return credentialEntity
        }

        override fun getRegion(): String {
            return region
        }

        override fun getGlobalRegion(): String {
            return globalRegion
        }

        override suspend fun threadSafe(): CredentialProvider {
            return this
        }

    }
}