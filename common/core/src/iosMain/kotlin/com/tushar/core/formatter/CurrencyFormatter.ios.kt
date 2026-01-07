package com.tushar.core.formatter

import com.tushar.core.model.BigDecimal
import platform.Foundation.NSLocale
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyStyle

/**
 * iOS implementation of CurrencyFormatter using NSNumberFormatter
 */
actual class CurrencyFormatter : CurrencyFormatterContract {
    actual override fun format(amount: BigDecimal, code: String): String {
        val formatter = NSNumberFormatter().apply {
            numberStyle = NSNumberFormatterCurrencyStyle
            currencyCode = code
            locale = NSLocale("en_US")
            maximumFractionDigits = 2u
            minimumFractionDigits = 2u
        }

        // Convert BigDecimal to Double for NSNumber
        val doubleValue = amount.toString().toDouble()
        val nsNumber = NSNumber(doubleValue)

        return formatter.stringFromNumber(nsNumber) ?: amount.toString()
    }
}
