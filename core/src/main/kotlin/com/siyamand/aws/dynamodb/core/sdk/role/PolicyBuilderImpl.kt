package com.siyamand.aws.dynamodb.core.sdk.role

import java.nio.file.Files
import java.nio.file.Paths

class PolicyBuilderImpl() : PolicyBuilder {

    override fun createLambdaPolicy(): CreatePolicyEntity {
        return createPolicy(
                "Monitoring-Lambda-Policy",
                "policies/LambdaPolicy.json",
                "This Policy has been created by Dynamodb monitoring tool. The policy contain necessary permissions for Lambda functions used by Monitoring-Dynamodb",
                "/dynamodbmonitoring/lambda/policy/")
    }
     override fun createLambdaEc2Policy(): CreatePolicyEntity {
        return createPolicy(
                "Monitoring-Lambda-Ec2-Policy",
                "policies/LambdaEc2Policy.json",
                "This Policy has been created by Dynamodb monitoring tool. The policy contain necessary permissions for Lambda functions to access VPC",
                "/dynamodbmonitoring/lambda/policy/ec2/")
    }

    override fun createRdsProxyPolicy(): CreatePolicyEntity {
        return createPolicy(
                "Rds-Proxy-Policy",
                "policies/RdsProxyPolicy.json",
                "This Policy has been created by Dynamodb monitoring tool. The policy contain necessary permissions for RDS Proxy to access security Manager values",
                "/dynamodbmonitoring/rds/policy/rdsproxy/")
    }

    override fun createLambdaSecretManagerPolicy(): CreatePolicyEntity {
        return createPolicy(
                "Monitoring-Lambda-SecretManager-Policy",
                "policies/SecretManagerPolicy.json",
                "This Policy has been created by Dynamodb monitoring tool. The policy contain necessary permissions for Lambda functions to use SecretManager",
                "/dynamodbmonitoring/lambda/policy/aecretmanagerpolicy/")
    }

    override fun createAccessRdsProxyPolicy(): CreatePolicyEntity {
        return createPolicy(
                "Monitoring-RdsProxy-Policy",
                "policies/AccessProxyPolicy.json",
                "This Policy has been created by Dynamodb monitoring tool. The policy contain necessary permissions for Rds Proxy used by Monitoring-Dynamodb",
                "/dynamodbmonitoring/rdsproxy/policy/")
    }

    override fun createAppConfigAccessPolicy(): CreatePolicyEntity {
        return createPolicy(
                "Monitoring-AppConfig-Access-Policy",
                "policies/AppConfigReadOnlyPolicy.json",
                "This Policy has been created by Dynamodb monitoring tool. The policy contain necessary permissions to have readonly access to Application Configuration",
                "/dynamodbmonitoring/appconfig/policy/")
    }

    private fun createPolicy(name: String, resourcePath: String, description: String, path: String): CreatePolicyEntityImpl {
        val uri = javaClass.classLoader.getResource(resourcePath).toURI()
        val policyDocument = Files.readString(Paths.get(uri))
        return CreatePolicyEntityImpl(
                name,
                policyDocument,
                description,
                path
        )
    }

    private class CreatePolicyEntityImpl(
            override val policyName: String,
            override val policyDocument: String,
            override val description: String,
            override val path: String) : CreatePolicyEntity
}