package com.tushar.data.repository

import com.tushar.data.datasource.remote.api.realtime.RealtimePriceUpdateService
import com.tushar.data.datasource.remote.api.realtime.model.PriceUpdateRequest.SocketPayload
import com.tushar.data.datasource.remote.api.realtime.model.PriceUpdateRequest.Subscribe
import com.tushar.data.datasource.remote.api.realtime.model.PriceUpdateRequest.Symbol
import com.tushar.data.repository.model.PriceUpdateRepoModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

interface RealtimePriceUpdateRepository {
    suspend fun connect(symbols: List<Symbol>)
    val priceUpdate: Flow<PriceUpdateRepoModel>
    suspend fun disconnect()
}

class RealtimePriceUpdateRepositoryImpl(
    private val realtimePriceUpdateService: RealtimePriceUpdateService,
    private val dispatcher: CoroutineDispatcher
) : RealtimePriceUpdateRepository {

    private val _priceUpdate = MutableSharedFlow<PriceUpdateRepoModel>(replay = 1)
    override val priceUpdate: Flow<PriceUpdateRepoModel> = _priceUpdate.asSharedFlow()

    override suspend fun connect(symbols: List<Symbol>) {
        subscribe(symbols)
        startListeningUpdate()
    }

    private suspend fun subscribe(symbols: List<Symbol>) {
        realtimePriceUpdateService.connect(
            Subscribe(
                payload = SocketPayload(
                    symbols = symbols
                )
            )
        )
    }

    private fun startListeningUpdate() {
        realtimePriceUpdateService.priceUpdate()
            .onEach { _priceUpdate.emit(it.asRepoModel()) }
            .launchIn(CoroutineScope(dispatcher))
    }

    override suspend fun disconnect() {
        realtimePriceUpdateService.disconnect()
    }
}
