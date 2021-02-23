package com.siyamand.aws.dynamodb.infrastructure.repositories

import com.siyamand.aws.dynamodb.core.kms.KmsRepository
import com.siyamand.aws.dynamodb.core.lambda.LambdaRepository
import com.siyamand.aws.dynamodb.infrastructure.ClientBuilder
import software.amazon.awssdk.services.kms.model.CreateKeyRequest

class KmsRepositoryImpl(private val clientBuilder: ClientBuilder) : KmsRepository, AwsBaseRepositoryImpl()  {

}