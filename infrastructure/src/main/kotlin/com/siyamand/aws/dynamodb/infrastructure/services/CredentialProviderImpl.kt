package com.siyamand.aws.dynamodb.infrastructure.services

import com.siyamand.aws.dynamodb.core.entities.BasicCredentialEntity
import com.siyamand.aws.dynamodb.core.entities.CredentialEntity
import com.siyamand.aws.dynamodb.core.entities.TokenCredentialEntity
import com.siyamand.aws.dynamodb.core.services.CredentialProvider
import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

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

        return null
    }

    override fun getRegion(): String {
        return "us-east-2"
    }

    override fun getGlobalRegion(): String {
        return "aws-global"
    }
}