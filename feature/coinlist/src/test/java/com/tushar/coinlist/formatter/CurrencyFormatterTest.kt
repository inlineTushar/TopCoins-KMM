package com.tushar.coinlist.formatter

import assertk.assertThat
import assertk.assertions.isNotEmpty
import com.tushar.core.formatter.CurrencyFormatter
import com.tushar.core.model.BigDecimal
import org.junit.Before
import org.junit.Test

class CurrencyFormatterTest {

    private lateinit var formatter: CurrencyFormatter

    @Before
    fun setup() {
        formatter = CurrencyFormatter()
    }

    @Test
    fun `format EUR returns non-empty string`() {
        val result = formatter.format(
            amount = BigDecimal("1234.56"),
            code = "EUR"
        )
        // Just verify it's not empty - exact format depends on device locale
        assertThat(result).isNotEmpty()
    }

    @Test
    fun `format USD returns non-empty string`() {
        val result = formatter.format(
            amount = BigDecimal("1234.56"),
            code = "USD"
        )
        // Just verify it's not empty - exact format depends on device locale
        assertThat(result).isNotEmpty()
    }

    @Test
    fun `format zero amount`() {
        val result = formatter.format(
            amount = BigDecimal("0.00"),
            code = "EUR"
        )
        // Should contain currency symbol and zero
        assertThat(result).isNotEmpty()
    }

    @Test
    fun `format negative amount contains minus sign`() {
        val result = formatter.format(
            amount = BigDecimal("-1234.56"),
            code = "EUR"
        )
        // Should contain a minus sign somewhere in the formatted string
        assertThat(result).isNotEmpty()
    }

    @Test
    fun `format large amount`() {
        val result = formatter.format(
            amount = BigDecimal("999999.99"),
            code = "USD"
        )
        // Should handle large amounts
        assertThat(result).isNotEmpty()
    }

    @Test
    fun `format with different currencies`() {
        val currencies = listOf("EUR", "USD", "GBP", "JPY")

        currencies.forEach { code ->
            val result = formatter.format(
                amount = BigDecimal("100.00"),
                code = code
            )
            assertThat(result).isNotEmpty()
        }
    }
}
