package com.tushar.core.formatter

import com.tushar.core.model.BigDecimal

/**
 * Interface for currency formatting - can be mocked in tests
 */
interface CurrencyFormatterContract {
    /**
     * Formats a BigDecimal amount into a currency string.
     *
     * @param amount The amount to format
     * @param code ISO 4217 currency code (e.g., "EUR", "USD")
     * @return Formatted currency string (e.g., "€1,234.56")
     */
    fun format(amount: BigDecimal, code: String): String
}

/**
 * Platform-specific currency formatter
 *
 * Android: Uses java.text.NumberFormat
 * iOS: Uses NSNumberFormatter
 */
expect class CurrencyFormatter() : CurrencyFormatterContract {
    override fun format(amount: BigDecimal, code: String): String
}
