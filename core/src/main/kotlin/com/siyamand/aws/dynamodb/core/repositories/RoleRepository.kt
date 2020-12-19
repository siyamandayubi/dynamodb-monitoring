package com.siyamand.aws.dynamodb.core.repositories

import com.siyamand.aws.dynamodb.core.entities.RoleEntity
import org.springframework.stereotype.Component

@Component
interface RoleRepository: AWSCRUDRepository<RoleEntity, RoleRepository> {
}