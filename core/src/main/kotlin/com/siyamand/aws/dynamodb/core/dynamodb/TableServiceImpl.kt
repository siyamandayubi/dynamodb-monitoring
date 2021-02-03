package com.siyamand.aws.dynamodb.core.dynamodb

import com.siyamand.aws.dynamodb.core.authentication.CredentialProvider

internal class TableServiceImpl(
        private val tableRepository: TableRepository,
        private val credentialProvider: CredentialProvider
) : TableService {

    override suspend fun getTables(): List<TableEntity> {
        val credential = credentialProvider.getCredential() ?: throw SecurityException("No Credential has been provided");

        tableRepository.initialize(credential, credentialProvider.getRegion());
        return  tableRepository.getList()
    }

    override suspend fun getTableDetail(tableName: String): TableDetailEntity? {
        val credential = credentialProvider.getCredential() ?: throw SecurityException("No Credential has been provided");

        tableRepository.initialize(credential, credentialProvider.getRegion());
        return  tableRepository.getDetail(tableName)
    }
}