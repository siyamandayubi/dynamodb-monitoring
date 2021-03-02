package com.siyamand.aws.dynamodb.infrastructure

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.appconfig.AppConfigAsyncClient
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.streams.DynamoDbStreamsAsyncClient
import software.amazon.awssdk.services.ec2.Ec2Client
import software.amazon.awssdk.services.iam.IamAsyncClient
import software.amazon.awssdk.services.iam.IamAsyncClientBuilder
import software.amazon.awssdk.services.kms.KmsAsyncClient
import software.amazon.awssdk.services.kms.KmsClientBuilder
import software.amazon.awssdk.services.lambda.LambdaAsyncClient
import software.amazon.awssdk.services.rds.RdsAsyncClient
import software.amazon.awssdk.services.resourcegroupstaggingapi.ResourceGroupsTaggingApiClient
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient
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

    override fun buildAsyncRdsClient(region: String, credential: AwsCredentialsProvider): RdsAsyncClient {
        return RdsAsyncClient.builder().region(Region.of(region)).credentialsProvider(credential).build()
    }

    override fun buildAsyncDynamodb(region: String, credential: AwsCredentialsProvider): DynamoDbAsyncClient {
        return DynamoDbAsyncClient.builder().region(Region.of(region)).credentialsProvider(credential).build()
    }

    override fun builcResourceGroupsTaggingApiClient(region: String, credential: AwsCredentialsProvider): ResourceGroupsTaggingApiClient {
        return ResourceGroupsTaggingApiClient.builder().region(Region.of(region)).credentialsProvider(credential).build()
    }

    override fun buildDynamoDbStreamsAsyncClient(region: String, credential: AwsCredentialsProvider): DynamoDbStreamsAsyncClient {
        return DynamoDbStreamsAsyncClient.builder().region(Region.of(region)).credentialsProvider(credential).build()
    }

    override fun buildAsyncS3Client(region: String, credential: AwsCredentialsProvider): S3AsyncClient {
        return S3AsyncClient.builder().region(Region.of(region)).credentialsProvider(credential).build()
    }

    override fun buildAsyncSecretsManagerClient(region: String, credential: AwsCredentialsProvider): SecretsManagerClient {
        return SecretsManagerClient.builder().region(Region.of(region)).credentialsProvider(credential).build()
    }

    override fun buildEc2Client(region: String, credential: AwsCredentialsProvider): Ec2Client {
        return Ec2Client.builder().region(Region.of(region)).credentialsProvider(credential).build()
    }

    override fun buildKmsAsyncClient(region: String, credential: AwsCredentialsProvider): KmsAsyncClient {
        return KmsAsyncClient.builder().region(Region.of(region)).credentialsProvider(credential).build()
    }

    override fun buildAppConfigAsyncClient(region: String, credential: AwsCredentialsProvider): AppConfigAsyncClient {
        return AppConfigAsyncClient.builder().region(Region.of(region)).credentialsProvider(credential).build()
    }
}