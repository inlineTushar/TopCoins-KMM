package com.tushar.coinlist.formatter

import kotlinx.datetime.Instant

/**
 * Platform-specific time formatter
 *
 * Android: Uses java.text.SimpleDateFormat
 * iOS: Uses NSDateFormatter
 */
expect class TimeFormatter() {
    /**
     * Formats an Instant into a time string.
     *
     * @param instant The instant to format
     * @param pattern Time pattern (default: "HH:mm:ss")
     * @return Formatted time string (e.g., "14:30:45")
     */
    fun format(instant: Instant, pattern: String = "HH:mm:ss"): String
}
