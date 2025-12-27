package com.tushar.core.model

import platform.Foundation.NSDecimalNumber

/**
 * iOS implementation using NSDecimalNumber from Foundation.
 * Uses string-based conversion for precision.
 */
actual class BigDecimal : Comparable<BigDecimal> {
    internal val value: NSDecimalNumber

    actual constructor(value: String) {
        this.value = NSDecimalNumber(string = value)
    }

    actual constructor(value: Double) {
        this.value = NSDecimalNumber(string = value.toString())
    }

    actual constructor(value: Int) {
        this.value = NSDecimalNumber(string = value.toString())
    }

    actual constructor(value: Long) {
        this.value = NSDecimalNumber(string = value.toString())
    }

    // Internal constructor for wrapping NSDecimalNumber
    internal constructor(nsValue: NSDecimalNumber) {
        this.value = nsValue
    }

    actual companion object {
        actual val ZERO: BigDecimal = BigDecimal("0")
        actual val ONE: BigDecimal = BigDecimal("1")
    }

    actual fun toDouble(): Double = value.doubleValue

    actual fun toPlainString(): String = value.stringValue

    actual override fun toString(): String = value.stringValue

    actual operator fun plus(other: BigDecimal): BigDecimal =
        BigDecimal(value.decimalNumberByAdding(other.value))

    actual operator fun minus(other: BigDecimal): BigDecimal =
        BigDecimal(value.decimalNumberBySubtracting(other.value))

    actual operator fun times(other: BigDecimal): BigDecimal =
        BigDecimal(value.decimalNumberByMultiplyingBy(other.value))

    actual operator fun div(other: BigDecimal): BigDecimal =
        BigDecimal(value.decimalNumberByDividingBy(other.value))

    actual override operator fun compareTo(other: BigDecimal): Int =
        value.compare(other.value).toInt()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BigDecimal) return false
        return value.compare(other.value).toInt() == 0
    }

    override fun hashCode(): Int = value.hashCode()
}

actual fun String.toBigDecimal(): BigDecimal = BigDecimal(this)
actual fun Double.toBigDecimal(): BigDecimal = BigDecimal(this)
actual fun Int.toBigDecimal(): BigDecimal = BigDecimal(this)
actual fun Long.toBigDecimal(): BigDecimal = BigDecimal(this)
