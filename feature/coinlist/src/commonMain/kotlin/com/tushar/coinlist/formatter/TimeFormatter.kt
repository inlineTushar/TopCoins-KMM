package com.tushar.coinlist.formatter

import kotlin.time.Instant

/**
 * Interface for time formatting - can be mocked in tests
 */
interface TimeFormatterContract {
    /**
     * Formats an Instant into a time string.
     *
     * @param instant The instant to format
     * @param pattern Time pattern (default: "HH:mm:ss")
     * @return Formatted time string (e.g., "14:30:45")
     */
    fun format(instant: Instant, pattern: String = "HH:mm:ss"): String
}

/**
 * Platform-specific time formatter
 *
 * Android: Uses java.text.SimpleDateFormat
 * iOS: Uses NSDateFormatter
 */
expect class TimeFormatter() : TimeFormatterContract {
    override fun format(instant: Instant, pattern: String): String
}
