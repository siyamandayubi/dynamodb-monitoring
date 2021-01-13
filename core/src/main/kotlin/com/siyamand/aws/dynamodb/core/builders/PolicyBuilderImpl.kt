package com.siyamand.aws.dynamodb.core.builders

import com.siyamand.aws.dynamodb.core.entities.CreatePolicyEntity
import java.nio.file.Files
import java.nio.file.Paths

class PolicyBuilderImpl() : PolicyBuilder {

    override fun createLambdaPolicy(): CreatePolicyEntity {
        val uri = javaClass.classLoader.getResource("policies/LambdaPolicy.json").toURI()
        val policyDocument = Files.readString(Paths.get(uri))
        return CreatePolicyEntityImpl(
                "Monitoring-Lambda-Policy",
                policyDocument,
                "This Policy has been created by Dynamodb monitoring tool. The policy contain necessary permissions for Lambda functions used by Monitoring-Dynamodb",
                "/dynamodbmonitoring/lambda/policy/"
        )
    }

    private class CreatePolicyEntityImpl(
            override val policyName: String,
            override val policyDocument: String,
            override val description: String,
            override val path: String) : CreatePolicyEntity
}