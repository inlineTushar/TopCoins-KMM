package com.tushar.coinlist.formatter

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterDecimalStyle
import kotlin.math.abs

/**
 * iOS implementation of PercentageFormatter using NSNumberFormatter
 */
actual class PercentageFormatter {
    actual fun format(percent: Double, fractionDigits: Int): String {
        val formatter = NSNumberFormatter().apply {
            numberStyle = NSNumberFormatterDecimalStyle
            maximumFractionDigits = fractionDigits.toULong()
            minimumFractionDigits = fractionDigits.toULong()
        }

        val absValue = abs(percent)
        val nsNumber = NSNumber(absValue)
        val formatted = formatter.stringFromNumber(nsNumber) ?: absValue.toString()

        return when {
            percent > 0 -> "+$formatted"
            percent < 0 -> "-$formatted"
            else -> "+$formatted"
        }
    }
}
