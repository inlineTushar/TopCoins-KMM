package com.tushar.coinlist.formatter

import kotlin.time.Instant
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Android implementation of TimeFormatter using Java's SimpleDateFormat
 */
actual class TimeFormatter : TimeFormatterContract {
    actual override fun format(instant: Instant, pattern: String): String {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        val date = Date(instant.toEpochMilliseconds())
        return formatter.format(date)
    }
}
