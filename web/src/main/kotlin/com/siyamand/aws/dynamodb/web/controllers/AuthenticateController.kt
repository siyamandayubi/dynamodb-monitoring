package com.siyamand.aws.dynamodb.web.controllers

import com.siyamand.aws.dynamodb.core.sdk.authentication.AuthenticationService
import com.siyamand.aws.dynamodb.web.models.CredentialModel
import com.siyamand.aws.dynamodb.web.models.TokenModel
import com.siyamand.aws.dynamodb.web.services.JwtSignerService
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.Duration
import java.time.Instant
import java.util.*

@CrossOrigin(maxAge = 3600)
@RestController
class AuthenticateController(private val jwtSignerService: JwtSignerService, private val authenticationService: AuthenticationService) {
    @PostMapping("/login")
    suspend fun login(@RequestBody user : CredentialModel): HttpEntity<TokenModel>{
        val response = authenticationService.getToken(user.keyId, user.secretKeyId)
        val expiredIn = response.expiredIn ?: Date.from(Instant.now().plus(Duration.ofMinutes(15)))
        val jwt = jwtSignerService.createJwt(response.accessKey,response.secretKey,response.sessionToken, expiredIn)
        return ResponseEntity(TokenModel(jwt, response.expiredIn), HttpStatus.OK)
    }
}