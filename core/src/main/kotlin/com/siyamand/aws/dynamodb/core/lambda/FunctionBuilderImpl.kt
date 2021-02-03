package com.siyamand.aws.dynamodb.core.lambda

import com.siyamand.aws.dynamodb.core.helpers.ZipHelper

class FunctionBuilderImpl : FunctionBuilder {
    override fun build(name: String, code: String, role :String): CreateFunctionRequestEntity {
        val output = CreateFunctionRequestEntity(
                name,
        "nodejs12.x",
                role,
                "index.handler",
                ZipHelper.zip(code, "index.js"),
                "",
                null,
                128,
                true,
                "Zip"
        )
        return  output
    }
}
