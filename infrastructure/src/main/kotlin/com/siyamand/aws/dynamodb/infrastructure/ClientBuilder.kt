package com.siyamand.aws.dynamodb.infrastructure

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.iam.IamAsyncClient
import software.amazon.awssdk.services.lambda.LambdaAsyncClient
import software.amazon.awssdk.services.resourcegroupstaggingapi.ResourceGroupsTaggingApiClient
import software.amazon.awssdk.services.sts.StsAsyncClient

interface ClientBuilder {
    fun buildAsyncDynamodb(region: String, credential: AwsCredentialsProvider): DynamoDbAsyncClient
    fun buildAsyncAwsLambda(region: String, credential: AwsCredentialsProvider): LambdaAsyncClient
    fun buildAmazonIdentityManagementAsyncClient(region: String, credential: AwsCredentialsProvider): IamAsyncClient
    fun buildSecurityTokenAsync(region: String, keyId: String, secretAcessId: String): StsAsyncClient
    fun builcResourceGroupsTaggingApiClient(region: String, credential: AwsCredentialsProvider): ResourceGroupsTaggingApiClient
}