package com.tushar.coinlist.formatter

import com.tushar.domain.model.BigDecimal

/**
 * Platform-specific currency formatter
 *
 * Android: Uses java.text.NumberFormat
 * iOS: Uses NSNumberFormatter
 */
expect class CurrencyFormatter() {
    /**
     * Formats a BigDecimal amount into a currency string.
     *
     * @param amount The amount to format
     * @param code ISO 4217 currency code (e.g., "EUR", "USD")
     * @return Formatted currency string (e.g., "€1,234.56")
     */
    fun format(amount: BigDecimal, code: String): String
}
