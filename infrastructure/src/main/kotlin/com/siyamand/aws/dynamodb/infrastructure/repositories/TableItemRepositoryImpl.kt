package com.siyamand.aws.dynamodb.infrastructure.repositories

import com.siyamand.aws.dynamodb.core.entities.CredentialEntity
import com.siyamand.aws.dynamodb.core.entities.TableItemEntity
import com.siyamand.aws.dynamodb.core.repositories.TableItemRepository
import com.siyamand.aws.dynamodb.core.repositories.TableRepository
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder

class TableItemRepositoryImpl(private val clientBuilder: ClientBuilder): TableItemRepository, AwsBaseRepositoryImpl() {
    override suspend fun add(tableItem: TableItemEntity) {
        TODO("Not yet implemented")
    }
}