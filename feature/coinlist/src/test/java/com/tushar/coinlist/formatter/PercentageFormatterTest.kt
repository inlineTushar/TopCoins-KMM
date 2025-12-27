package com.tushar.coinlist.formatter

import com.tushar.core.formatter.PercentageFormatter
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PercentageFormatterTest {

    private lateinit var formatter: PercentageFormatter

    @Before
    fun setup() {
        formatter = PercentageFormatter()
    }

    @Test
    fun `format positive percentage`() {
        val result = formatter.format(
            percent = 5.23
        )
        assertEquals("+5.23", result)
    }

    @Test
    fun `format negative percentage`() {
        val result = formatter.format(
            percent = -2.45
        )
        assertEquals("-2.45", result)
    }

    @Test
    fun `format zero percentage`() {
        val result = formatter.format(
            percent = 0.0
        )
        assertEquals("+0.00", result)
    }

    @Test
    fun `format with custom fraction digits`() {
        val result = formatter.format(
            percent = 5.23456,
            fractionDigits = 3
        )
        assertEquals("+5.235", result)
    }

    @Test
    fun `format with one fraction digit`() {
        val result = formatter.format(
            percent = 5.23,
            fractionDigits = 1
        )
        assertEquals("+5.2", result)
    }

    @Test
    fun `format rounds correctly`() {
        val result = formatter.format(
            percent = 5.996,
            fractionDigits = 2
        )
        assertEquals("+6.00", result)
    }
}
