package com.tushar.data.datasource.remote.instrumentation

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.math.BigDecimal

object BigDecimalSerializer : KSerializer<BigDecimal> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(
            serialName = "BigDecimalSerializer",
            kind = PrimitiveKind.STRING
        )

    override fun deserialize(decoder: Decoder): BigDecimal {
        val rawDecimal = decoder.decodeString()
        return BigDecimal(rawDecimal)
    }

    override fun serialize(encoder: Encoder, value: BigDecimal) {
        encoder.encodeString(value.toString())
    }
}