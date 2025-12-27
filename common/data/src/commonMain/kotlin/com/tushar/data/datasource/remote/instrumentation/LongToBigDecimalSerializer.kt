package com.tushar.data.datasource.remote.instrumentation

import com.tushar.core.model.BigDecimal
import com.tushar.core.model.toBigDecimal
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object LongToBigDecimalSerializer : KSerializer<BigDecimal> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(
            serialName = "BigDecimalSerializer",
            kind = PrimitiveKind.DOUBLE
        )

    override fun deserialize(decoder: Decoder): BigDecimal {
        val rawDecimal = decoder.decodeDouble()
        return rawDecimal.toBigDecimal()
    }

    override fun serialize(encoder: Encoder, value: BigDecimal) {
        encoder.encodeString(value.toString())
    }
}