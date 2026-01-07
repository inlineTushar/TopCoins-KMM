package com.tushar.core.formatter

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.tushar.core.model.BigDecimal
import kotlin.test.BeforeTest
import kotlin.test.Test

class CurrencyFormatterTest {

    private lateinit var formatter: CurrencyFormatterContract

    @BeforeTest
    fun setup() {
        formatter = CurrencyFormatter()
    }

    @Test
    fun `format returns correct string for different currencies`() {
        // Test with EUR
        assertThat(formatter.format(BigDecimal("1234.56"), "EUR"))
            .isEqualTo("€1,234.56")

        // Test with USD
        assertThat(formatter.format(BigDecimal("1234.56"), "USD"))
            .isEqualTo("$1,234.56")

        // Test with GBP
        assertThat(formatter.format(BigDecimal("100.00"), "GBP"))
            .isEqualTo("£100.00")

        // Test with JPY
        assertThat(formatter.format(BigDecimal("100.00"), "JPY"))
            .isEqualTo("¥100.00")
    }

    @Test
    fun `format zero amount`() {
        assertThat(formatter.format(BigDecimal("0.00"), "EUR"))
            .isEqualTo("€0.00")

        assertThat(formatter.format(BigDecimal("0.00"), "USD"))
            .isEqualTo("$0.00")
    }

    @Test
    fun `format negative amount`() {
        assertThat(formatter.format(BigDecimal("-1234.56"), "EUR"))
            .isEqualTo("-€1,234.56")
    }

    @Test
    fun `format large amount`() {
        assertThat(formatter.format(BigDecimal("999999.99"), "USD"))
            .isEqualTo("$999,999.99")
    }
}