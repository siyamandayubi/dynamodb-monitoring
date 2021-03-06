package com.siyamand.aws.dynamodb.core.services

import com.siyamand.aws.dynamodb.core.entities.TableDetailEntity
import com.siyamand.aws.dynamodb.core.entities.TableEntity
import com.siyamand.aws.dynamodb.core.repositories.TableRepository
import com.siyamand.aws.dynamodb.core.repositories.TokenRepository

internal class TableServiceImpl(
        private val tableRepository: TableRepository,
        private val credentialProvider: CredentialProvider
) : TableService {

    override suspend fun getTables(): List<TableEntity> {
        val credential = credentialProvider.getCredential() ?: throw SecurityException("No Credential has been provided");

        tableRepository.withToken(credential);
        return  tableRepository.getList()
    }

    override suspend fun getTableDetail(tableName: String): TableDetailEntity? {
        val credential = credentialProvider.getCredential() ?: throw SecurityException("No Credential has been provided");

        tableRepository.withToken(credential);
        return  tableRepository.getDetail(tableName)
    }
}