package com.tushar.domain.repository

import com.tushar.domain.model.CoinsInUsdDomainModel
import com.tushar.domain.model.ConversionRateDomainModel
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for accessing coin and conversion rate data.
 */
interface CoinRepository {
    /**
     * Gets a flow of coins with prices in USD along with timestamp.
     *
     * @param refresh If true, forces a refresh from remote data source
     * @return Flow emitting coins with USD prices and timestamp
     */
    fun getCoins(refresh: Boolean = false): Flow<CoinsInUsdDomainModel>

    /**
     * Gets the conversion rate for a target currency.
     *
     * @param refresh If true, forces a refresh from remote data source
     * @param targetCurrency The currency to get conversion rate for
     * @return Flow emitting the conversion rate
     */
    fun getRate(
        refresh: Boolean = false,
        targetCurrency: String
    ): Flow<ConversionRateDomainModel>
}
