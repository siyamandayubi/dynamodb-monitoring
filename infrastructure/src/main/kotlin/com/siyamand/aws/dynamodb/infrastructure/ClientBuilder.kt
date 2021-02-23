package com.siyamand.aws.dynamodb.infrastructure

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.streams.DynamoDbStreamsAsyncClient
import software.amazon.awssdk.services.ec2.Ec2Client
import software.amazon.awssdk.services.iam.IamAsyncClient
import software.amazon.awssdk.services.kms.KmsAsyncClient
import software.amazon.awssdk.services.lambda.LambdaAsyncClient
import software.amazon.awssdk.services.rds.RdsAsyncClient
import software.amazon.awssdk.services.resourcegroupstaggingapi.ResourceGroupsTaggingApiClient
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient
import software.amazon.awssdk.services.sts.StsAsyncClient

interface ClientBuilder {
    fun buildAsyncRdsClient(region: String, credential: AwsCredentialsProvider): RdsAsyncClient
    fun buildAsyncDynamodb(region: String, credential: AwsCredentialsProvider): DynamoDbAsyncClient
    fun buildAsyncAwsLambda(region: String, credential: AwsCredentialsProvider): LambdaAsyncClient
    fun buildAmazonIdentityManagementAsyncClient(region: String, credential: AwsCredentialsProvider): IamAsyncClient
    fun buildSecurityTokenAsync(region: String, keyId: String, secretAcessId: String): StsAsyncClient
    fun builcResourceGroupsTaggingApiClient(region: String, credential: AwsCredentialsProvider): ResourceGroupsTaggingApiClient
    fun buildDynamoDbStreamsAsyncClient(region: String, credential: AwsCredentialsProvider): DynamoDbStreamsAsyncClient
    fun buildAsyncS3Client(region: String, credential: AwsCredentialsProvider): S3AsyncClient
    fun buildAsyncSecretsManagerClient(region: String, credential: AwsCredentialsProvider): SecretsManagerClient
    fun buildEc2Client(region: String, credential: AwsCredentialsProvider): Ec2Client
    fun buildKmsAsyncClient(region: String, credential: AwsCredentialsProvider): KmsAsyncClient
}