package com.siyamand.aws.dynamodb.core.sdk.appconfig.entities

class CreateEnvironmentEntity(val applicationId: String, val name: String, val description: String, val tags: Map<String, String>)