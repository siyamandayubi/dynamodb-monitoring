package com.siyamand.aws.dynamodb.core.builders

import com.siyamand.aws.dynamodb.core.entities.CreatePolicyEntity
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

    override fun createRdsProxyPolicy(): CreatePolicyEntity {
        return createPolicy(
                "Monitoring-RdsProxy-Policy",
                "policies/ProxyPolicy.json",
                "This Policy has been created by Dynamodb monitoring tool. The policy contain necessary permissions for Rds Proxy used by Monitoring-Dynamodb",
                "/dynamodbmonitoring/rdsproxy/policy/")
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