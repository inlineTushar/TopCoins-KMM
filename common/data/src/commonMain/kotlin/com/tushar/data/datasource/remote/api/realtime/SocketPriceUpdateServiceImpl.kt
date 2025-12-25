package com.tushar.data.datasource.remote.api.realtime

import com.tushar.data.datasource.remote.api.realtime.model.PriceUpdateApiResponse
import com.tushar.data.datasource.remote.api.realtime.model.PriceUpdateRequest
import com.tushar.data.datasource.remote.api.realtime.model.PriceUpdateRequestSerializer
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlin.coroutines.cancellation.CancellationException

class SocketPriceUpdateServiceImpl(
    private val json: Json,
    private val socketClient: HttpClient,
    private val priceUpdateUrl: String,
) : RealtimePriceUpdateService {

    private var socketSession: WebSocketSession? = null

    override suspend fun connect(request: PriceUpdateRequest) {
        if (socketSession != null) return
        runCatching {
            socketSession = socketClient.webSocketSession(priceUpdateUrl)
            send(json.encodeToString(PriceUpdateRequestSerializer, request))
        }.onFailure { e -> if (e is CancellationException) throw e else print(e) }
    }

    private fun createStream(): Flow<PriceUpdateApiResponse> =
        socketSession
            ?.incoming
            ?.consumeAsFlow()
            ?.filterIsInstance<Frame.Text>()
            ?.map { frame -> frame.toPriceUpdate() }
            ?: flowOf()

    private suspend fun send(message: String) {
        socketSession?.send(Frame.Text(message))
    }

    private fun Frame.Text.toPriceUpdate() =
        json.decodeFromString<PriceUpdateApiResponse>(this.readText())

    override suspend fun disconnect() {
        socketSession?.close()
        socketSession = null
    }

    override fun priceUpdate(): Flow<PriceUpdateApiResponse> {
        requireNotNull(socketSession) { "Socket connection was not established. Call connect() first " }
        return createStream()
    }
}
