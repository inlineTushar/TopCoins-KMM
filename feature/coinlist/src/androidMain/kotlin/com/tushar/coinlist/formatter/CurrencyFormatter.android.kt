package com.tushar.coinlist.formatter

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import com.tushar.domain.model.BigDecimal as KmmBigDecimal
import java.math.BigDecimal as JavaBigDecimal

/**
 * Android implementation of CurrencyFormatter using Java's NumberFormat
 */
actual class CurrencyFormatter {
    actual fun format(amount: KmmBigDecimal, code: String): String {
        // Convert KMM BigDecimal to Java BigDecimal
        val javaAmount = JavaBigDecimal(amount.toString())

        val numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault()).apply {
            currency = Currency.getInstance(code)
            maximumFractionDigits = 2
            minimumFractionDigits = 2
        }
        return numberFormat.format(javaAmount)
    }
}
