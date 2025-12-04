package com.tushar.domain

/**
 * Common network exceptions for cross-platform use
 */
expect open class UnknownHostException(message: String? = null) : Exception

expect open class ConnectException(message: String? = null) : Exception

expect open class SocketTimeoutException(message: String? = null) : Exception
