package com.tushar.coinlist.formatter

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

interface CurrencyFormatter {
    /**
     * Formats a [BigDecimal] amount into a currency string.
     *
     * @param amount The amount to format
     * @param currencyCode ISO 4217 currency code (e.g., "EUR", "USD")
     * @param locale The locale to use for formatting. Defaults to system default.
     * @return Formatted currency string (e.g., "€1,234.56")
     */
    fun format(
        amount: BigDecimal,
        code: String,
        locale: Locale = Locale.getDefault()
    ): String
}

/**
 * Default implementation of [CurrencyFormatter].
 */
class DefaultCurrencyFormatter : CurrencyFormatter {

    override fun format(
        amount: BigDecimal,
        code: String,
        locale: Locale
    ): String {
        val numberFormat = NumberFormat.getCurrencyInstance(locale).apply {
            currency = Currency.getInstance(code)
            maximumFractionDigits = 2
            minimumFractionDigits = 2
        }
        return numberFormat.format(amount)
    }
}
