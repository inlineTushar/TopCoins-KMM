package com.tushar.coinlist.formatter

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.tushar.coinlist.formatter.CurrencyFormatter
import com.tushar.coinlist.formatter.DefaultCurrencyFormatter
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.util.Locale

class CurrencyFormatterTest {

    private lateinit var formatter: CurrencyFormatter

    @Before
    fun setup() {
        formatter = DefaultCurrencyFormatter()
    }

    @Test
    fun `format EUR with German locale`() {
        val result = formatter.format(
            amount = BigDecimal("1234.56"),
            code = "EUR",
            locale = Locale.GERMANY
        )
        // German locale uses non-breaking space (U+00A0) before €
        assertThat(result).isEqualTo("1.234,56\u00A0€")
    }

    @Test
    fun `format EUR with US locale`() {
        val result = formatter.format(
            amount = BigDecimal("1234.56"),
            code = "EUR",
            locale = Locale.US
        )
        assertThat(result).isEqualTo("€1,234.56")
    }

    @Test
    fun `format USD with US locale`() {
        val result = formatter.format(
            amount = BigDecimal("1234.56"),
            code = "USD",
            locale = Locale.US
        )
        assertThat(result).isEqualTo("$1,234.56")
    }

    @Test
    fun `format rounds to 2 decimal places`() {
        val result = formatter.format(
            amount = BigDecimal("1234.56789"),
            code = "EUR",
            locale = Locale.US
        )
        assertThat(result).isEqualTo("€1,234.57")
    }

    @Test
    fun `format zero amount`() {
        val result = formatter.format(
            amount = BigDecimal.ZERO,
            code = "EUR",
            locale = Locale.US
        )
        assertThat(result).isEqualTo("€0.00")
    }

    @Test
    fun `format negative amount`() {
        val result = formatter.format(
            amount = BigDecimal("-1234.56"),
            code = "EUR",
            locale = Locale.US
        )
        assertThat(result).isEqualTo("-€1,234.56")
    }
}
