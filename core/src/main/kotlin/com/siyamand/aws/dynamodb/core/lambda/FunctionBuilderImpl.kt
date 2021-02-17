package com.siyamand.aws.dynamodb.core.lambda

import com.siyamand.aws.dynamodb.core.helpers.ZipHelper
import java.nio.file.Files
import java.nio.file.Paths

class FunctionBuilderImpl : FunctionBuilder {
    companion object {
        const val NODEJS_RUNTIME = "nodejs12.x"
    }

    override fun build(name: String, code: String, role: String, vararg layers: String): CreateFunctionRequestEntity {
        return CreateFunctionRequestEntity(
                name,
                NODEJS_RUNTIME,
                role,
                "index.handler",
                ZipHelper.zip(code, "index.js"),
                "",
                null,
                128,
                true,
                "Zip",
                layers = layers.toList()
        )
    }

    override fun buildLayer(name: String, description: String, resourcePath: String): CreateLayerEntity {
        val uri = javaClass.classLoader.getResource(resourcePath).toURI()
        val bytes = Files.readAllBytes(Paths.get(uri))
        return CreateLayerEntity(name, NODEJS_RUNTIME, bytes, description, "MIT Licence")
    }
}
