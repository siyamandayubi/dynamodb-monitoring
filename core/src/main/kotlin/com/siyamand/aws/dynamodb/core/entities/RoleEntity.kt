package com.siyamand.aws.dynamodb.core.entities

class RoleEntity(var name: String) {
}

class CreateRoleEntity(
        val roleName: String? = null,
        val assumeRolePolicyDocument: String? = null,
        val description: String? = null,
        val maxSessionDuration: Int? = null,
        val permissionsBoundary: String? = null)

class CreatePolicyEntity(
        val policyName: String? = null,
        val policyDocument: String? = null,
        val description: String? = null)