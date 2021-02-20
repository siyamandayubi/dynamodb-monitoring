package com.siyamand.aws.dynamodb.infrastructure.mappers

import com.siyamand.aws.dynamodb.core.lambda.*
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.services.lambda.model.*
import java.nio.charset.Charset
import java.util.*

class FunctionMapper {
    companion object {

        fun convert(function: software.amazon.awssdk.services.lambda.model.FunctionConfiguration): FunctionEntity {
            return FunctionEntity(
                    function.functionName(),
                    function.runtimeAsString(),
                    function.role(),
                    function.description(),
                    function.packageTypeAsString(),
                    function.handler(),
                    function.timeout(),
                    function.memorySize())
        }

        fun convert(functionCode: FunctionCode): FunctionCodeEntity {
            val zipFile: String? = functionCode.zipFile()?.asString(Charset.defaultCharset())
            return FunctionCodeEntity(
                    zipFile,
                    functionCode.s3Bucket(),
                    functionCode.s3Key(),
                    functionCode.s3ObjectVersion(),
                    functionCode.imageUri())
        }

        fun convert(response: PublishLayerVersionResponse): FunctionLayerEntity {
            return FunctionLayerEntity(
                    ResourceMapper.convert(response.layerVersionArn()),
                    response.description() ?: "",
                    response.createdDate(),
                    response.version(),
                    response.compatibleRuntimesAsStrings(),
                    response.licenseInfo()
            )
        }

        fun convert(response: LayerVersionsListItem): FunctionLayerEntity {
            return FunctionLayerEntity(
                    ResourceMapper.convert(response.layerVersionArn()),
                    response.description() ?: "",
                    response.createdDate(),
                    response.version(),
                    response.compatibleRuntimesAsStrings(),
                    response.licenseInfo()
            )
        }

        fun convert(functionCode: FunctionCodeLocation): FunctionCodeLocationEntity {
            return FunctionCodeLocationEntity(
                    functionCode.repositoryType(),
                    functionCode.location(),
                    functionCode.resolvedImageUri(),
                    functionCode.imageUri())
        }

        fun convert(functionResponse: GetFunctionResponse): FunctionDetailEntity {
            return FunctionDetailEntity(convert(functionResponse.configuration()), convert(functionResponse.code()))
        }

        fun convert(entity: CreateLayerEntity): PublishLayerVersionRequest {
            return PublishLayerVersionRequest
                    .builder()
                    .layerName(entity.layerName)
                    .licenseInfo(entity.licenseInfo)
                    .description(entity.description)
                    .compatibleRuntimes(Runtime.fromValue(entity.runTime))
                    .content(LayerVersionContentInput.builder().zipFile(SdkBytes.fromByteArray(entity.code)).build())
                    .build()
        }

        fun convert(entity: CreateEventSourceRequestEntity): CreateEventSourceMappingRequest {
            val builder = CreateEventSourceMappingRequest.builder()
                    .functionName(entity.functionName)
                    .batchSize(entity.batchSize)
                    .enabled(entity.enabled)
                    .eventSourceArn(entity.eventSourceArn)
                    .maximumBatchingWindowInSeconds(entity.maximumBatchingWindowInSeconds)
                    .maximumRecordAgeInSeconds(entity.maximumRecordAgeInSeconds)
                    .maximumRetryAttempts(entity.maximumRetryAttempts)
                    .startingPosition(entity.startingPosition)
                    .startingPositionTimestamp(entity.startingPositionTimestamp)
                    .parallelizationFactor(entity.parallelizationFactor)
                    .bisectBatchOnFunctionError(entity.bisectBatchOnFunctionError)

            return builder.build()
        }

        fun convert(entity: CreateFunctionRequestEntity): CreateFunctionRequest {
            val encodedCode = Base64.getEncoder().encode(entity.code)
            val builder = CreateFunctionRequest.builder()
                    .functionName(entity.functionName)
                    .code(FunctionCode.builder().zipFile(SdkBytes.fromByteArray(entity.code)).build())
                    .description(entity.description)
                    .handler(entity.handler)
                    .environment(Environment.builder().variables(entity.environmentVariables).build())
                    .runtime(entity.runtime)
                    .packageType(entity.packageType)
                    .memorySize(entity.memorySize)
                    .role(entity.role)
                    .publish(entity.publish)
                    .tags(entity.tags)

            if (entity.layers.any()) {
                builder.layers(entity.layers)
            }

            return builder.build()
        }
    }
}