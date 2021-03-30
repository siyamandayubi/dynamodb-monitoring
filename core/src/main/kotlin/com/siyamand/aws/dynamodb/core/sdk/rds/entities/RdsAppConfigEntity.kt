package com.siyamand.aws.dynamodb.core.sdk.rds.entities

import com.siyamand.aws.dynamodb.core.common.PrimitiveSerialDescriptor
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.*

@Serializable
class RdsAppConfigEntity {
    var endpoints: MutableList<RdsAppConfigItemEntity> = mutableListOf()
    var databaseName: String = ""
}

@Serializable(with = RdsAppConfigItemEntitySerializer::class)
class RdsAppConfigItemEntity {
    var arn: String = ""
    var endPoint: String = ""
    var port: Int = 3306
    var start: String = "000"
    var end: String = "000"
    var order: Int = 0
}

class RdsAppConfigItemEntitySerializer : KSerializer<RdsAppConfigItemEntity> {
    @ExperimentalSerializationApi
    override val descriptor: SerialDescriptor =
            buildClassSerialDescriptor("RdsAppConfigItemEntity") {
                element("arn", PrimitiveSerialDescriptor("r", PrimitiveKind.STRING))
                element("endPoint", PrimitiveSerialDescriptor("endPoint", PrimitiveKind.STRING))
                element("port", PrimitiveSerialDescriptor("port", PrimitiveKind.INT))
                element("start", PrimitiveSerialDescriptor("start", PrimitiveKind.STRING))
                element("end", PrimitiveSerialDescriptor("end", PrimitiveKind.STRING))
                element("order", PrimitiveSerialDescriptor("order", PrimitiveKind.INT))
            }

    @ExperimentalSerializationApi
    override fun serialize(encoder: Encoder, value: RdsAppConfigItemEntity) {
        encoder.encodeStructure(descriptor)
        {
            encodeStringElement(descriptor, 0, value.arn)
            encodeStringElement(descriptor, 1, value.endPoint)
            encodeIntElement(descriptor, 2, value.port)
            encodeStringElement(descriptor, 3, value.start)
            encodeStringElement(descriptor, 4, value.end)
            encodeIntElement(descriptor, 5, value.order)
        }
    }

    override fun deserialize(decoder: Decoder): RdsAppConfigItemEntity {
        decoder.decodeStructure(descriptor) {
            val returnValue = RdsAppConfigItemEntity()
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> returnValue.arn = decodeStringElement(descriptor, index)
                    1 -> returnValue.endPoint = decodeStringElement(descriptor, index)
                    2 -> returnValue.port = decodeIntElement(descriptor, index)
                    3 -> returnValue.start = decodeStringElement(descriptor, index)
                    4 -> returnValue.end = decodeStringElement(descriptor, index)
                    5 -> returnValue.port = decodeIntElement(descriptor, index)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("unexpected index $index")
                }
            }

            return returnValue
        }
    }
}