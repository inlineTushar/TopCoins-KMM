package com.tushar.coinlist.formatter

import kotlinx.datetime.Instant
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale

/**
 * iOS implementation of TimeFormatter using NSDateFormatter
 */
actual class TimeFormatter {
    actual fun format(instant: Instant, pattern: String): String {
        val formatter = NSDateFormatter().apply {
            dateFormat = pattern
            locale = NSLocale.currentLocale
        }

        // Convert Instant to NSDate
        val timeInterval = instant.epochSeconds.toDouble()
        val nsDate = NSDate(timeInterval)

        return formatter.stringFromDate(nsDate)
    }
}
