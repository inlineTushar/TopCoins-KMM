package com.tushar.data.datasource.remote.api.realtime

import com.tushar.data.datasource.remote.api.RemoteService
import com.tushar.data.datasource.remote.api.realtime.model.PriceUpdateApiResponse
import com.tushar.data.datasource.remote.api.realtime.model.PriceUpdateRequest
import kotlinx.coroutines.flow.Flow

interface RealtimePriceUpdateService : RemoteService {
    suspend fun connect(request: PriceUpdateRequest)
    suspend fun disconnect()
    fun priceUpdate(): Flow<PriceUpdateApiResponse>
}
