package com.tushar.domain

/**
 * iOS implementations of network exceptions
 */
actual open class UnknownHostException actual constructor(message: String?) : Exception(message) {
    constructor() : this(null)
}

actual open class ConnectException actual constructor(message: String?) : Exception(message) {
    constructor() : this(null)
}

actual open class SocketTimeoutException actual constructor(message: String?) : Exception(message) {
    constructor() : this(null)
}
