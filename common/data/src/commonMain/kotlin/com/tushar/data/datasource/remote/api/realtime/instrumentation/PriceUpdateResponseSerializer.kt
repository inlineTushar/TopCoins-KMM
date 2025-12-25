package com.tushar.data.datasource.remote.api.realtime.instrumentation

import com.tushar.data.datasource.remote.api.realtime.model.PriceUpdateApiResponse
import com.tushar.data.datasource.remote.api.realtime.model.PriceUpdateTick
import com.tushar.data.datasource.remote.api.realtime.model.SubscribeStatusResponse
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object PriceUpdateResponseSerializer : KSerializer<PriceUpdateApiResponse> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("PriceUpdateResponse")

    override fun deserialize(decoder: Decoder): PriceUpdateApiResponse {
        val jsonDecoder = decoder as? JsonDecoder
            ?: error("PriceUpdateResponseSerializer supports JSON only")

        val element = jsonDecoder.decodeJsonElement()
        val event = element.jsonObject["event"]?.jsonPrimitive?.content
            ?: error("Missing 'event' field")

        return when (event) {
            "subscribe-status" -> jsonDecoder.json.decodeFromJsonElement(
                    SubscribeStatusResponse.serializer(),
                    element
                )

            "price" -> jsonDecoder.json.decodeFromJsonElement(
                    PriceUpdateTick.serializer(),
                    element
                )

            else -> error("Unknown event type: $event")
        }
    }

    override fun serialize(
        encoder: Encoder,
        value: PriceUpdateApiResponse
    ) {
        val jsonEncoder = encoder as? JsonEncoder ?: error("JSON only")

        val jsonElement = when (value) {
            is SubscribeStatusResponse ->
                jsonEncoder.json.encodeToJsonElement(
                    SubscribeStatusResponse.serializer(),
                    value
                )

            is PriceUpdateTick ->
                jsonEncoder.json.encodeToJsonElement(
                    PriceUpdateTick.serializer(),
                    value
                )
        }

        jsonEncoder.encodeJsonElement(jsonElement)
    }
}