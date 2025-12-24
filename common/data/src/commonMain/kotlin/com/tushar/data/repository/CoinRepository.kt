package com.tushar.data.repository

import com.tushar.data.datasource.remote.api.http.CoinApiService
import com.tushar.data.repository.model.CoinsRepoModel
import com.tushar.data.repository.model.ConversionRateRepoModel
import com.tushar.domain.model.CoinsInUsdDomainModel
import com.tushar.domain.model.ConversionRateDomainModel
import com.tushar.domain.repository.CoinRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.withTimeout

class CoinRepositoryImpl(
    private val remoteService: CoinApiService,
    private val dispatcher: CoroutineDispatcher
) : CoinRepository {
    private val coins = MutableStateFlow<CoinsRepoModel?>(null)
    private val rate = MutableStateFlow<ConversionRateRepoModel?>(null)

    override fun getCoins(
        refresh: Boolean
    ): Flow<CoinsInUsdDomainModel> = flow {
        val cached = coins.value
        if (!refresh && cached != null) {
            emit(cached)
            return@flow
        }

        fetchCoins()
            .retryWhen { cause, attempt -> shouldRetry(cause, attempt, TAG_GET_COINS) }
            .onEach { data -> coins.value = data }
            .collect { data -> emit(data) }
    }.map { it.asDomainModel() }.flowOn(dispatcher)

    override fun getRate(
        refresh: Boolean,
        targetCurrency: String
    ): Flow<ConversionRateDomainModel> = flow {
        val cached = rate.value
        if (!refresh && cached != null) {
            emit(cached)
            return@flow
        }

        fetchConversionRate(targetCurrency)
            .retryWhen { cause, attempt -> shouldRetry(cause, attempt, TAG_GET_RATE) }
            .onEach { data -> rate.value = data }
            .collect { data -> emit(data) }
    }.map { it.asDomainModel() }.flowOn(dispatcher)

    /**
     * Creates a Flow that fetches coin data from remote API.
     */
    private fun fetchCoins(): Flow<CoinsRepoModel> = flow {
        val result = withTimeout(NETWORK_TIMEOUT_MS) {
            remoteService.getCoins().asRepoModel()
        }
        emit(result)
    }

    /**
     * Creates a Flow that fetches rate data from remote API.
     */
    private fun fetchConversionRate(targetCurrency: String): Flow<ConversionRateRepoModel> = flow {
        val result = withTimeout(NETWORK_TIMEOUT_MS) {
            remoteService.getRates(targetCurrency).asRepoModel()
        }
        emit(result)
    }

    companion object {
        private const val TAG_GET_COINS = "get_coins"
        private const val TAG_GET_RATE = "conversion_rate"
    }
}
