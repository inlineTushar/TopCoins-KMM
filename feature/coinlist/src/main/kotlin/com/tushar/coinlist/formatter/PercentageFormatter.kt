package com.tushar.coinlist.formatter

import java.util.Locale

interface PercentageFormatter {
    /**
     * Formats a percentage value with a sign prefix.
     *
     * @param percent The percentage value (e.g., 5.23 for 5.23%)
     * @param fractionDigits Number of decimal places. Defaults to 2.
     * @param locale The locale to use for formatting. Defaults to system default.
     * @return Formatted percentage string with sign (e.g., "+5.23" or "-2.45")
     */
    fun format(
        percent: Double,
        fractionDigits: Int = 2,
        locale: Locale = Locale.getDefault()
    ): String
}

class DefaultPercentageFormatter : PercentageFormatter {

    override fun format(
        percent: Double,
        fractionDigits: Int,
        locale: Locale
    ): String = String.format(locale, "%+.${fractionDigits}f", percent)
}
