package com.tushar.coinlist.formatter

import kotlin.time.Instant
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.time.ExperimentalTime

class TimeFormatterTest {

    private lateinit var formatter: TimeFormatterContract

    @BeforeTest
    fun setup() {
        formatter = TimeFormatter()
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun `format timestamp with default pattern`() {
        // 2024-01-15 14:30:45 UTC
        val instant = Instant.fromEpochMilliseconds(1705329045000L)
        val result = formatter.format(instant = instant)

        // Result should match HH:mm:ss pattern (8 characters with colons)
        assertTrue(result.matches(Regex("\\d{2}:\\d{2}:\\d{2}")))
    }

    @Test
    fun `format timestamp with custom pattern`() {
        // 2024-01-15 14:30:45 UTC
        val instant = Instant.fromEpochMilliseconds(1705329045000L)
        val result = formatter.format(
            instant = instant,
            pattern = "HH:mm"
        )

        // Result should match HH:mm pattern (5 characters with colon)
        assertTrue(result.matches(Regex("\\d{2}:\\d{2}")))
    }

    @Test
    fun `format timestamp with 12-hour pattern`() {
        // 2024-01-15 14:30:45 UTC
        val instant = Instant.fromEpochMilliseconds(1705329045000L)
        val result = formatter.format(
            instant = instant,
            pattern = "hh:mm:ss a"
        )

        // Result should match hh:mm:ss a pattern and contain AM or PM
        assertTrue(result.matches(Regex("\\d{2}:\\d{2}:\\d{2} (AM|PM)")))
    }

    @Test
    fun `format midnight`() {
        // 2024-01-15 00:00:00 UTC
        val instant = Instant.fromEpochMilliseconds(1705276800000L)
        val result = formatter.format(instant = instant)

        // Result should be a valid time format
        assertTrue(result.matches(Regex("\\d{2}:\\d{2}:\\d{2}")))
    }

    @Test
    fun `format with date pattern`() {
        val instant = Instant.fromEpochMilliseconds(1705329045000L)
        val result = formatter.format(
            instant = instant,
            pattern = "yyyy-MM-dd"
        )

        // Result should match yyyy-MM-dd pattern
        assertTrue(result.matches(Regex("\\d{4}-\\d{2}-\\d{2}")))
    }

    @Test
    fun `format with full datetime pattern`() {
        val instant = Instant.fromEpochMilliseconds(1705329045000L)
        val result = formatter.format(
            instant = instant,
            pattern = "yyyy-MM-dd HH:mm:ss"
        )

        // Result should match full datetime pattern
        assertTrue(result.matches(Regex("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")))
    }
}