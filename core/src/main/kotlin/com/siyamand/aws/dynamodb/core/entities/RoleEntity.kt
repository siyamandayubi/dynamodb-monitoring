package com.siyamand.aws.dynamodb.core.entities

class RoleEntity(
        var name: String,
        val resource: ResourceEntity,
        val description: String?,
        val path: String,
        val assumeRolePolicyDocument: String?
) {
}

interface CreateRoleEntity {
    val roleName: String
    val assumeRolePolicyDocument: String?
    val description: String?
    val maxSessionDuration: Int?
    val permissionsBoundary: String?
    val tags: MutableList<TagEntity>
}

interface CreatePolicyEntity {
    val policyName: String
    val policyDocument: String
    val description: String
    val path: String
}
