package com.siyamand.aws.dynamodb.web.services

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.security.authentication.CredentialsExpiredException
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.time.Instant
import java.util.*


class JwtSignerServiceImpl(private val environment: Environment) : JwtSignerService {

    val logger: Logger = LoggerFactory.getLogger("com.siyamand.aws.dynamodb.web.services.JwtSignerServiceImpl")
    override fun createJwt(userId: String, secretKey: String, sessionToken: String, date: Date): String {
        //https://medium.com/@jaidenashmore/jwt-authentication-in-spring-boot-webflux-6880c96247c7
        val privateKeyStr: String? = environment.getProperty("sec_private_key")

        val kf: KeyFactory = KeyFactory.getInstance("RSA") // or "EC" or whatever

        val privateKey: PrivateKey? = kf.generatePrivate(PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyStr)))        //val privateKey = RSAPrivateKeyImpl(Base64.getDecoder().decode(privateKeyStr))
        val finalDate = date

        return Jwts.builder()
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .setSubject(userId)
                .setClaims(mapOf(Pair(TokenClaims.ACCESS_KEY, userId), Pair(TokenClaims.SECRET_KEY, secretKey), Pair(TokenClaims.SESSION_TOKEN, sessionToken)))
                .setIssuer("identity")
                .setExpiration(finalDate)
                .setIssuedAt(Date.from(Instant.now()))
                .compact()
    }

    /**
     * Validate the JWT where it will throw an exception if it isn't valid.
     */
    override fun validateJwt(jwt: String): Jws<Claims> {
        try {
            val publicKeyStr: String? = environment.getProperty("sec_public_key")
            val kf: KeyFactory = KeyFactory.getInstance("RSA") // or "EC" or whatever

            val publicKey: PublicKey? = kf.generatePublic(X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyStr)))        //val privateKey = RSAPrivateKeyImpl(Base64.getDecoder().decode(privateKeyStr))
            return Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(jwt)
        } catch (exception: Exception) {
            logger.error(exception.message, exception)
            throw CredentialsExpiredException(exception.message, exception)
        }
    }
}