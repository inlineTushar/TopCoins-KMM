package com.tushar.core.formatter

import com.tushar.core.model.BigDecimal
import java.math.BigDecimal as JavaBigDecimal
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

/**
 * Android implementation of CurrencyFormatter using Java'''s NumberFormat
 */
actual class CurrencyFormatter : CurrencyFormatterContract {
    actual override fun format(amount: BigDecimal, code: String): String {
        // Convert KMM BigDecimal to Java BigDecimal
        val javaAmount = JavaBigDecimal(amount.toString())

        val numberFormat = NumberFormat.getCurrencyInstance(Locale.US).apply {
            currency = Currency.getInstance(code)
            maximumFractionDigits = 2
            minimumFractionDigits = 2
        }
        return numberFormat.format(javaAmount)
    }
}