package com.tushar.coinlist.formatter

/**
 * Platform-specific percentage formatter
 *
 * Android: Uses java.text.DecimalFormat
 * iOS: Uses NSNumberFormatter
 */
expect class PercentageFormatter() {
    /**
     * Formats a Double value into a percentage string.
     *
     * @param percent The value to format (e.g., 5.23 for 5.23%)
     * @param fractionDigits Number of fraction digits (default: 2)
     * @return Formatted percentage string (e.g., "+5.23")
     */
    fun format(percent: Double, fractionDigits: Int = 2): String
}
