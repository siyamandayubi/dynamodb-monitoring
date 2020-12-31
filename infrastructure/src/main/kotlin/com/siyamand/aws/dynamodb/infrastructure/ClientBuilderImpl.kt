package com.siyamand.aws.dynamodb.infrastructure

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.iam.IamAsyncClient
import software.amazon.awssdk.services.iam.IamAsyncClientBuilder
import software.amazon.awssdk.services.lambda.LambdaAsyncClient
import software.amazon.awssdk.services.resourcegroupstaggingapi.ResourceGroupsTaggingApiClient
import software.amazon.awssdk.services.sts.StsAsyncClient

class ClientBuilderImpl : ClientBuilder {
    override fun buildAmazonIdentityManagementAsyncClient(region: String, credential: AwsCredentialsProvider): IamAsyncClient {

        return IamAsyncClient.builder()
                .credentialsProvider(credential)
                .region(Region.of(region))
                .build()
    }

    override fun buildSecurityTokenAsync(region: String, keyId: String, secretAcessId: String): StsAsyncClient {

        return StsAsyncClient
                .builder()
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(keyId, secretAcessId)))
                .region(Region.of(region))
                .build()
    }

    override fun buildAsyncAwsLambda(region: String, credential: AwsCredentialsProvider): LambdaAsyncClient {

        return LambdaAsyncClient.builder().region(Region.of(region)).credentialsProvider(credential).build()
    }

    override fun buildAsyncDynamodb(region: String, credential: AwsCredentialsProvider): DynamoDbAsyncClient {
        return DynamoDbAsyncClient.builder().region(Region.of(region)).credentialsProvider(credential).build()
    }

    override fun builcResourceGroupsTaggingApiClient(region: String, credential: AwsCredentialsProvider): ResourceGroupsTaggingApiClient {
        return ResourceGroupsTaggingApiClient.builder().region(Region.of(region)).credentialsProvider(credential).build()
    }
}