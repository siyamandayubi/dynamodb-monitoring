package com.siyamand.aws.dynamodb.core.builders

import com.siyamand.aws.dynamodb.core.entities.CreateFunctionRequestEntity
import com.siyamand.aws.dynamodb.core.helpers.ZipHelper
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

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
