package com.siyamand.aws.dynamodb.infrastructure.repositories

import com.siyamand.aws.dynamodb.core.sdk.kms.KmsRepository
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder

class KmsRepositoryImpl(private val clientBuilder: ClientBuilder) : KmsRepository, AwsBaseRepositoryImpl()  {

}