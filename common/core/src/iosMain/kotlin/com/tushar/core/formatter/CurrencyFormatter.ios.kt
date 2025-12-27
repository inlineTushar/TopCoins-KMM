package com.tushar.core.formatter

import com.tushar.core.model.BigDecimal
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyStyle
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale

/**
 * iOS implementation of CurrencyFormatter using NSNumberFormatter
 */
actual class CurrencyFormatter {
    actual fun format(amount: BigDecimal, code: String): String {
        val formatter = NSNumberFormatter().apply {
            numberStyle = NSNumberFormatterCurrencyStyle
            currencyCode = code
            locale = NSLocale.currentLocale
            maximumFractionDigits = 2u
            minimumFractionDigits = 2u
        }

        // Convert BigDecimal to Double for NSNumber
        val doubleValue = amount.toString().toDouble()
        val nsNumber = NSNumber(doubleValue)

        return formatter.stringFromNumber(nsNumber) ?: amount.toString()
    }
}
