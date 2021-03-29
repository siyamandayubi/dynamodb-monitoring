package com.siyamand.aws.dynamodb.core.common

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


class InstantSerializer() : KSerializer<Instant> {
    @ExperimentalSerializationApi
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.from(ZoneOffset.UTC))

    override fun deserialize(decoder: Decoder): Instant {
        val dt: LocalDateTime = formatter.parse(decoder.decodeString(), LocalDateTime::from)
        return dt.toInstant(ZoneOffset.UTC)
    }

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString(formatter.format(value))
    }
}

@ExperimentalSerializationApi
internal class PrimitiveSerialDescriptor(
        @ExperimentalSerializationApi
        override val serialName: String,

        @ExperimentalSerializationApi
        override val kind: PrimitiveKind
) : SerialDescriptor {
    @ExperimentalSerializationApi
    override val elementsCount: Int get() = 0

    @ExperimentalSerializationApi
    override fun getElementName(index: Int): String = error()

    @ExperimentalSerializationApi
    override fun getElementIndex(name: String): Int = error()

    @ExperimentalSerializationApi
    override fun isElementOptional(index: Int): Boolean = error()

    @ExperimentalSerializationApi
    override fun getElementDescriptor(index: Int): SerialDescriptor = error()

    @ExperimentalSerializationApi
    override fun getElementAnnotations(index: Int): List<Annotation> = error()

    override fun toString(): String = "PrimitiveDescriptor($serialName)"
    private fun error(): Nothing = throw IllegalStateException("Primitive descriptor does not have elements")
}