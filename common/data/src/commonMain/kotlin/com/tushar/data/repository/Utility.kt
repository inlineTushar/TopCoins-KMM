package com.tushar.data.repository

import com.tushar.domain.SocketTimeoutException
import com.tushar.domain.UnknownHostException
import kotlinx.coroutines.delay
import kotlinx.io.IOException
import kotlin.coroutines.cancellation.CancellationException
import kotlin.math.min
import kotlin.math.pow

const val NETWORK_TIMEOUT_MS = 30_000L
const val MAX_RETRY_ATTEMPTS = 3
private const val INITIAL_RETRY_DELAY_MS = 1000L
private const val MAX_RETRY_DELAY_MS = 10_000L

suspend fun shouldRetry(cause: Throwable, attempt: Long, tag: String): Boolean {
    if (cause is CancellationException) throw cause

    val isRetry = isExceptionAllowedForRetry(cause)
    val shouldRetry = isRetry && attempt < MAX_RETRY_ATTEMPTS

    if (shouldRetry) {
        val delayMs = calculateBackoffDelay(attempt)
        delay(delayMs)
    }
    return shouldRetry
}

private fun isExceptionAllowedForRetry(cause: Throwable): Boolean = when (cause) {
    is UnknownHostException,
    is SocketTimeoutException,
    is IOException -> true

    else -> false
}

private fun calculateBackoffDelay(attempt: Long): Long {
    val exponentialDelay = (INITIAL_RETRY_DELAY_MS * 2.0.pow(attempt.toInt())).toLong()
    return min(exponentialDelay, MAX_RETRY_DELAY_MS)
}
