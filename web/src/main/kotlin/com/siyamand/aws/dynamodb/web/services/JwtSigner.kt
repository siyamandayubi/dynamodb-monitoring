package com.siyamand.aws.dynamodb.web.services

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.security.KeyPair
import java.time.Duration
import java.time.Instant
import java.util.*

//import io.jsonwebtoken.security.Keys
class JwtSigner {
    val keyPair: KeyPair = Keys.keyPairFor(SignatureAlgorithm.RS256)

    //https://medium.com/@jaidenashmore/jwt-authentication-in-spring-boot-webflux-6880c96247c7
    fun createJwt(userId: String): String {
        return Jwts.builder()
                .signWith(keyPair.private, SignatureAlgorithm.RS256)
                .setSubject(userId)
                .setIssuer("identity")
                .setExpiration(Date.from(Instant.now().plus(Duration.ofMinutes(15))))
                .setIssuedAt(Date.from(Instant.now()))
                .compact()
    }

    /**
     * Validate the JWT where it will throw an exception if it isn't valid.
     */
   fun validateJwt(jwt: String): Jws<Claims> {
        return Jwts.parserBuilder()
                .setSigningKey(keyPair.public)
                .build()
                .parseClaimsJws(jwt)
    }
}