package com.tushar.domain

import java.net.UnknownHostException as JavaUnknownHostException
import java.net.ConnectException as JavaConnectException
import java.net.SocketTimeoutException as JavaSocketTimeoutException

/**
 * Android implementations wrapping java.net exceptions
 */
actual open class UnknownHostException actual constructor(message: String?) : Exception(message) {
    constructor() : this(null)

    // For compatibility with Java exceptions
    companion object {
        fun fromJava(e: JavaUnknownHostException) = UnknownHostException(e.message)
    }
}

actual open class ConnectException actual constructor(message: String?) : Exception(message) {
    constructor() : this(null)

    companion object {
        fun fromJava(e: JavaConnectException) = ConnectException(e.message)
    }
}

actual open class SocketTimeoutException actual constructor(message: String?) : Exception(message) {
    constructor() : this(null)

    companion object {
        fun fromJava(e: JavaSocketTimeoutException) = SocketTimeoutException(e.message)
    }
}
