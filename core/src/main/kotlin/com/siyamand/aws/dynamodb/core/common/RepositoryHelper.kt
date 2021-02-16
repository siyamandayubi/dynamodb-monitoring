package com.siyamand.aws.dynamodb.core.common

import com.siyamand.aws.dynamodb.core.authentication.CredentialProvider

suspend fun CredentialProvider.initializeRepositories(vararg repositories: AWSBaseRepository) {
    val credential = this.getCredential()
            ?: throw SecurityException("No Credential has been provided");

    for (repository in repositories) {
        repository.initialize(credential, this.getRegion());
    }
}

suspend fun CredentialProvider.initializeRepositoriesWithGlobalRegion(vararg repositories: AWSBaseRepository) {
    val credential = this.getCredential()
            ?: throw SecurityException("No Credential has been provided");

    for (repository in repositories) {
        repository.initialize(credential, this.getGlobalRegion());
    }
}