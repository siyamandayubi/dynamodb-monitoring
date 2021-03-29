package com.siyamand.aws.dynamodb.core.workflow

object Keys {
    const val EVENT_SOURCE_ARN = "eventSourceArn"
    const val SOURCE_DYNAMODB_ARN = "sourceDynamodbArn"
    const val STREAM__DYNAMODB_ARN = "streamDynamodbArn"
    const val LAMBDA_ARN = "lambdaArn"
    const val PROXY_ARN_KEY = "rdsProxy"
    const val PROXY_NAME = "rdsProxyName"
    const val SECRET_ARN_KEY = "secretArn"
    const val RDS_ARN_KEY = "rdsArn"
    const val LAMBDA_LAYER_ARN_KEY = "lambdaLayerArn"
    const val LAMBDA_LAYER_PATH = "lambdaLayerPath"
    const val LAMBDA_LAYER_NAME = "lambdaLayerName"
    const val FORCE_CREATE = "forceCreate"
    const val DATABASE_NAME = "databaseName"
    const val DB_INSTANCE_CLASS = "instanceClass"
    const val PROXY_TARGET_GROUP_NAME = "rdsProxyTargetGroupName"
    const val CODE_RESULT = "codeResult"
    const val LAMBDA_ROLE = "lambdaRole"
    const val PROXY_TARGET_GROUP_ARN = "rdsProxyTargetGroupArn"
    const val DEPLOYMNET_NUMBER = "appConfigDeploymentArn"
}