package com.tushar.coinlist.formatter

import java.text.DecimalFormat

/**
 * Android implementation of PercentageFormatter using Java's DecimalFormat
 */
actual class PercentageFormatter : PercentageFormatterContract {
    actual override fun format(percent: Double, fractionDigits: Int): String {
        val pattern = buildString {
            append("0.")
            repeat(fractionDigits) { append("0") }
        }

        val formatter = DecimalFormat(pattern)
        val formatted = formatter.format(kotlin.math.abs(percent))

        return when {
            percent > 0 -> "+$formatted"
            percent < 0 -> "-$formatted"
            else -> "+$formatted"
        }
    }
}
