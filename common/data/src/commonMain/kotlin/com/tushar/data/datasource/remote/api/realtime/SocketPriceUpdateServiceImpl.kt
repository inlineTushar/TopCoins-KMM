package com.tushar.data.datasource.remote.api.realtime

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class SocketPriceUpdateServiceImpl(
    private val socketClient: HttpClient,
    private val priceUpdateUrl: String,
    private val scope: CoroutineScope
) : RealtimePriceUpdateService {

    private val _update = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 64
    )
    override val update: Flow<String> = _update.asSharedFlow()

    private var socketSession: WebSocketSession? = null
    private var job: Job? = null

    init {
        update
            .onEach { s -> println("sss $s") }
            .launchIn(scope)
    }

    override suspend fun connect(symbol: String) {
        if (socketSession != null) return
        socketSession = socketClient.webSocketSession(priceUpdateUrl)

        job = scope.launch {
            try {
                socketSession?.incoming
                    ?.consumeAsFlow()
                    ?.filterIsInstance<Frame.Text>()
                    ?.collect { frame ->
                        _update.emit(frame.readText())
                    }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
            }
        }

        delay(2000)
        send(
            """
                        {
                          "action": "subscribe",
                          "params": { "symbols": "BTC/USD" }
                        }
                        """.trimIndent()
        )
    }

    override suspend fun send(message: String) {
        socketSession?.send(Frame.Text(message))
            ?: error("WebSocket is not connected")
    }

    override suspend fun disconnect() {
        job?.cancel()
        job = null
        socketSession?.close()
        socketSession = null
    }
}
