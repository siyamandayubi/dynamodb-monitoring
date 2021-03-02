package com.siyamand.aws.dynamodb.web.controllers

import com.siyamand.aws.dynamodb.core.sdk.authentication.BasicCredentialEntity
import com.siyamand.aws.dynamodb.core.sdk.role.RoleEntity
import com.siyamand.aws.dynamodb.core.sdk.role.RoleService
import com.siyamand.aws.dynamodb.web.models.CredentialModel
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RoleController(private val roleService: RoleService) {
    @GetMapping("/api/roles")
    suspend fun getRoles(): HttpEntity<List<RoleEntity>> {
        val roles = this.roleService.getRoles();
        return ResponseEntity(roles, HttpStatus.OK)
    }

    @PostMapping("/api/roles/createLambdaRole")
    suspend fun createLambdaRole(@RequestBody credentialModel: CredentialModel): HttpEntity<RoleEntity> {
        val role = this.roleService.getOrCreateLambdaRole(BasicCredentialEntity(credentialModel.keyId, credentialModel.secretKeyId, null))
        return ResponseEntity(role, HttpStatus.OK)
    }

    @PostMapping("/api/roles/createRdsProxyRole")
    suspend fun createRdsProxyRole(@RequestBody credentialModel: CredentialModel): HttpEntity<RoleEntity> {
        val role = this.roleService.getOrCreateRdsProxyRole(BasicCredentialEntity(credentialModel.keyId, credentialModel.secretKeyId, null))
        return ResponseEntity(role, HttpStatus.OK)
    }
}