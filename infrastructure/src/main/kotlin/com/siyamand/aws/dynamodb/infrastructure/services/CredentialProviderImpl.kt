package com.siyamand.aws.dynamodb.infrastructure.services

import com.siyamand.aws.dynamodb.core.entities.BasicCredentialEntity
import com.siyamand.aws.dynamodb.core.entities.CredentialEntity
import com.siyamand.aws.dynamodb.core.entities.TokenCredentialEntity
import com.siyamand.aws.dynamodb.core.services.CredentialProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

class CredentialProviderImpl : CredentialProvider {

    @Autowired
    private val env: Environment? = null
 /*   private val currentRequest: HttpServletRequest?
        get() = (RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes)?.request
*/
    override fun getCredential(): CredentialEntity? {
       /* val request = currentRequest;
        val requestToken: TokenCredentialEntity? = request?.getAttribute(RequestServletAttributes.TOKEN) as TokenCredentialEntity?;
        if (requestToken != null) {
            return requestToken;
        }*/

        val awsAccessKey: String? = env?.getProperty("aws_access_key_id");
        val awsSecretKey: String? = env?.getProperty("aws_secret_access_key");

        if (awsAccessKey != null && awsSecretKey != null) {
            return BasicCredentialEntity(awsAccessKey, awsSecretKey, null)
        }

        return null
    }
}