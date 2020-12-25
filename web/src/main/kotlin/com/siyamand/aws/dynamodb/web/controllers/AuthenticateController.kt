package com.siyamand.aws.dynamodb.web.controllers

import com.siyamand.aws.dynamodb.core.services.AuthenticationService
import com.siyamand.aws.dynamodb.web.models.CredentialModel
import com.siyamand.aws.dynamodb.web.services.JwtSignerService
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticateController(private val jwtSignerService: JwtSignerService, private val authenticationService: AuthenticationService) {
    @PostMapping("/login")
    suspend fun login(@RequestBody user : CredentialModel): HttpEntity<String>{
        val response = authenticationService.getToken(user.keyId, user.secretKeyId)
        val jwt = jwtSignerService.createJwt(response.accessKey,response.secretKey,response.sessionToken, response.expiredIn)
        return ResponseEntity(jwt, HttpStatus.OK)
    }
}