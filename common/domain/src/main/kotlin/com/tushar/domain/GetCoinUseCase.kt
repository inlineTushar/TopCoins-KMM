package com.tushar.domain

import com.tushar.domain.model.CoinInUsdDomainModel
import com.tushar.domain.model.CoinsDomainModel
import com.tushar.domain.repository.CoinRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.cancellation.CancellationException

/**
 * Use case for retrieving and sorting cryptocurrency data.
 */
class GetCoinUseCase(
    private val repository: CoinRepository
) {
    /**
     * Gets top coins sorted by best 24-hour price change (descending).
     */
    suspend fun sortByBestPriceChange(
        refresh: Boolean = false,
        topCount: Int = TOP_COUNT,
    ): Result<CoinsDomainModel> =
        getSortedTopCoins(refresh, topCount, BEST_PERFORMANCE_COMPARATOR)

    /**
     * Gets top coins sorted by worst 24-hour price change (ascending).
     */
    suspend fun sortByWorstPriceChange(
        refresh: Boolean = false,
        topCount: Int = TOP_COUNT,
    ): Result<CoinsDomainModel> =
        getSortedTopCoins(refresh, topCount, WORST_PERFORMANCE_COMPARATOR)


    private suspend fun getSortedTopCoins(
        refresh: Boolean,
        topCount: Int,
        sortBy: Comparator<CoinInUsdDomainModel>
    ): Result<CoinsDomainModel> = try {
        combine(
            repository.getCoins(refresh),
            repository.getRate(refresh, DEFAULT_CURRENCY)
        ) { coinsInUsd, rateData ->
            CoinsDomainModel(
                timestamp = coinsInUsd.timestamp,
                coins = coinsInUsd.coins
                    .sortedWith(sortBy)
                    .take(topCount)
                    .asPriceAdjusted(rateData)
            )
        }.flowOn(Dispatchers.IO)
            .first()
            .let { Result.success(it) }
    } catch (e: Throwable) {
        if (e is CancellationException) throw e
        Result.failure(e.asDomainError())
    }

    companion object {
        private const val TOP_COUNT = 10
        private const val DEFAULT_CURRENCY = "euro"
        private val BEST_PERFORMANCE_COMPARATOR =
            compareByDescending<CoinInUsdDomainModel> { it.changePercent24Hr }

        private val WORST_PERFORMANCE_COMPARATOR =
            compareBy<CoinInUsdDomainModel> { it.changePercent24Hr }
    }
}
