package com.tushar.coinlist.formatter

/**
 * Interface for percentage formatting - can be mocked in tests
 */
interface PercentageFormatterContract {
    /**
     * Formats a Double value into a percentage string.
     *
     * @param percent The value to format (e.g., 5.23 for 5.23%)
     * @param fractionDigits Number of fraction digits (default: 2)
     * @return Formatted percentage string (e.g., "+5.23")
     */
    fun format(percent: Double, fractionDigits: Int = 2): String
}

/**
 * Platform-specific percentage formatter
 *
 * Android: Uses java.text.DecimalFormat
 * iOS: Uses NSNumberFormatter
 */
expect class PercentageFormatter() : PercentageFormatterContract {
    override fun format(percent: Double, fractionDigits: Int): String
}
