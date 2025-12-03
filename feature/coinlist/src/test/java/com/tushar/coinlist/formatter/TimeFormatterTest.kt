package com.tushar.coinlist.formatter

import com.tushar.coinlist.formatter.DefaultTimeFormatter
import com.tushar.coinlist.formatter.TimeFormatter
import kotlinx.datetime.Instant
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.ZoneId

class TimeFormatterTest {

    private lateinit var formatter: TimeFormatter

    @Before
    fun setup() {
        formatter = DefaultTimeFormatter()
    }

    @Test
    fun `format timestamp with default pattern`() {
        // 2024-01-15 14:30:45 UTC
        val timestamp = Instant.fromEpochMilliseconds(1705329045000L)
        val result = formatter.format(
            timestamp = timestamp,
            zoneId = ZoneId.of("UTC")
        )
        assertEquals("14:30:45", result)
    }

    @Test
    fun `format timestamp with custom pattern`() {
        // 2024-01-15 14:30:45 UTC
        val timestamp = Instant.fromEpochMilliseconds(1705329045000L)
        val result = formatter.format(
            timestamp = timestamp,
            pattern = "HH:mm",
            zoneId = ZoneId.of("UTC")
        )
        assertEquals("14:30", result)
    }

    @Test
    fun `format timestamp with 12-hour pattern`() {
        // 2024-01-15 14:30:45 UTC
        val timestamp = Instant.fromEpochMilliseconds(1705329045000L)
        val result = formatter.format(
            timestamp = timestamp,
            pattern = "hh:mm:ss a",
            zoneId = ZoneId.of("UTC")
        )
        assertEquals("02:30:45 PM", result)
    }

    @Test
    fun `format timestamp with different timezone`() {
        // 2024-01-15 14:30:45 UTC = 15:30:45 in Europe/Berlin (UTC+1)
        val timestamp = Instant.fromEpochMilliseconds(1705329045000L)
        val result = formatter.format(
            timestamp = timestamp,
            zoneId = ZoneId.of("Europe/Berlin")
        )
        assertEquals("15:30:45", result)
    }

    @Test
    fun `format midnight`() {
        // 2024-01-15 00:00:00 UTC
        val timestamp = Instant.fromEpochMilliseconds(1705276800000L)
        val result = formatter.format(
            timestamp = timestamp,
            zoneId = ZoneId.of("UTC")
        )
        assertEquals("00:00:00", result)
    }
}
