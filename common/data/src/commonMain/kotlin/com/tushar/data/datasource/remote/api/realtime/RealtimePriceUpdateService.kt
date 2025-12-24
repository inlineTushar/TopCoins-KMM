package com.tushar.data.datasource.remote.api.realtime

import com.tushar.data.datasource.remote.api.RemoteService
import kotlinx.coroutines.flow.Flow

interface RealtimePriceUpdateService : RemoteService {
    suspend fun connect(symbol: String)
    suspend fun disconnect()
    suspend fun send(message: String)
    val update: Flow<String>
}
