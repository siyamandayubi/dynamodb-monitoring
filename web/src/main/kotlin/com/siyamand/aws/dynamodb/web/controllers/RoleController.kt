package com.siyamand.aws.dynamodb.web.controllers

import com.siyamand.aws.dynamodb.core.entities.ResourceEntity
import com.siyamand.aws.dynamodb.core.entities.RoleEntity
import com.siyamand.aws.dynamodb.core.services.RoleService
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RoleController(private val roleService: RoleService) {
    @GetMapping("/api/roles")
    suspend fun getRoles(): HttpEntity<List<RoleEntity>>{
        val roles = this.roleService.getRoles();
        return ResponseEntity(roles, HttpStatus.OK)
    }

    @PostMapping("/api/roles/createLambdaRole")
    suspend fun createLambdaRole(): HttpEntity<ResourceEntity>{
        val role = this.roleService.createLambdaRole()
        return ResponseEntity(role, HttpStatus.OK)
    }
}