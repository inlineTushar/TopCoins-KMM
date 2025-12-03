package com.tushar.coinlist.formatter

import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

interface TimeFormatter {
    /**
     * Formats a timestamp (milliseconds since epoch) into a time string.
     *
     * @param timestampMillis The timestamp in milliseconds since epoch
     * @param pattern The date/time pattern (e.g., "HH:mm:ss")
     * @param zoneId The timezone to use. Defaults to system default.
     * @return Formatted time string (e.g., "14:30:45")
     */
    fun format(
        timestamp: Instant,
        pattern: String = "HH:mm:ss",
        zoneId: ZoneId = ZoneId.systemDefault()
    ): String
}

/**
 * Default implementation of [TimeFormatter].
 */
@Singleton
class DefaultTimeFormatter @Inject constructor() : TimeFormatter {

    override fun format(
        timestamp: Instant,
        pattern: String,
        zoneId: ZoneId
    ): String = timestamp
        .toJavaInstant()
        .atZone(zoneId)
        .toLocalTime()
        .format(DateTimeFormatter.ofPattern(pattern))
}
