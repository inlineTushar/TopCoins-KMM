package com.tushar.coinlist.formatter

import kotlin.time.Instant
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale

/**
 * iOS implementation of TimeFormatter using NSDateFormatter
 */
actual class TimeFormatter : TimeFormatterContract {
    actual override fun format(instant: Instant, pattern: String): String {
        val formatter = NSDateFormatter().apply {
            dateFormat = pattern
            // Use US locale for consistent output across platforms
            locale = NSLocale("en_US_POSIX")
        }

        // Convert Instant to NSDate
        val timeInterval = instant.epochSeconds.toDouble()
        val nsDate = NSDate(timeInterval)

        return formatter.stringFromDate(nsDate)
    }
}
