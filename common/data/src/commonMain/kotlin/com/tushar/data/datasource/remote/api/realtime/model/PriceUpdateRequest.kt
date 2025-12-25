package com.tushar.data.datasource.remote.api.realtime.model

import com.tushar.data.datasource.remote.api.realtime.model.SocketAction.SUBSCRIBE
import com.tushar.data.datasource.remote.api.realtime.model.SocketAction.UNSUBSCRIBE
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import kotlin.jvm.JvmInline

@Serializable(with = PriceUpdateRequestSerializer::class)
sealed class PriceUpdateRequest {
    abstract val action: SocketAction

    @Serializable
    data class Subscribe(
        @SerialName("params")
        val payload: SocketPayload
    ) : PriceUpdateRequest() {
        override val action: SocketAction = SUBSCRIBE
    }

    @Serializable
    data class SocketPayload(
        val symbols: List<Symbol>
    )

    @JvmInline
    @Serializable
    value class Symbol(val id: String)

    @Serializable
    data object Unsubscribe : PriceUpdateRequest() {
        override val action: SocketAction = UNSUBSCRIBE
    }

    @Serializable
    data object Reset : PriceUpdateRequest() {
        override val action: SocketAction = SocketAction.RESET
    }

    @Serializable
    data object Heartbeat : PriceUpdateRequest() {
        override val action: SocketAction = SocketAction.HEARTBEAT
    }
}

enum class SocketAction(val value: String) {
    SUBSCRIBE("subscribe"),
    UNSUBSCRIBE("unsubscribe"),
    RESET("reset"),
    HEARTBEAT("heartbeat")
}

object PriceUpdateRequestSerializer : KSerializer<PriceUpdateRequest> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("PriceUpdateSocketRequest")

    override fun serialize(
        encoder: Encoder,
        value: PriceUpdateRequest
    ) {
        require(encoder is JsonEncoder)
        val jsonObject = when (value) {
            is PriceUpdateRequest.Subscribe -> {
                buildJsonObject {
                    put("action", value.action.value)
                    putJsonObject("params") {
                        put(
                            "symbols",
                            value.payload.symbols.joinToString(",") { it.id }
                        )
                    }
                }
            }

            PriceUpdateRequest.Unsubscribe,
            PriceUpdateRequest.Reset,
            PriceUpdateRequest.Heartbeat -> {
                buildJsonObject { put("action", value.action.value) }
            }
        }

        encoder.encodeJsonElement(jsonObject)
    }

    override fun deserialize(decoder: Decoder): PriceUpdateRequest {
        error("Deserialization not supported")
    }
}
