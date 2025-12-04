package com.tushar.domain.model

import java.math.BigDecimal as JavaBigDecimal

/**
 * Android/JVM implementation using java.math.BigDecimal
 * Using a wrapper class to match the expected API exactly
 */
actual class BigDecimal : Comparable<BigDecimal> {
    internal val value: JavaBigDecimal

    actual constructor(value: String) {
        this.value = JavaBigDecimal(value)
    }

    actual constructor(value: Double) {
        this.value = JavaBigDecimal.valueOf(value)
    }

    actual constructor(value: Int) {
        this.value = JavaBigDecimal.valueOf(value.toLong())
    }

    actual constructor(value: Long) {
        this.value = JavaBigDecimal.valueOf(value)
    }

    // Internal constructor for wrapping Java BigDecimal
    internal constructor(javaValue: JavaBigDecimal) {
        this.value = javaValue
    }

    actual companion object {
        actual val ZERO: BigDecimal = BigDecimal(JavaBigDecimal.ZERO)
        actual val ONE: BigDecimal = BigDecimal(JavaBigDecimal.ONE)
    }

    actual fun toDouble(): Double = value.toDouble()

    actual fun toPlainString(): String = value.toPlainString()

    actual override fun toString(): String = value.toString()

    actual operator fun plus(other: BigDecimal): BigDecimal = BigDecimal(value.add(other.value))

    actual operator fun minus(other: BigDecimal): BigDecimal =
        BigDecimal(value.subtract(other.value))

    actual operator fun times(other: BigDecimal): BigDecimal =
        BigDecimal(value.multiply(other.value))

    actual operator fun div(other: BigDecimal): BigDecimal = BigDecimal(value.divide(other.value))

    actual override operator fun compareTo(other: BigDecimal): Int = value.compareTo(other.value)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BigDecimal) return false
        return value.compareTo(other.value) == 0
    }

    override fun hashCode(): Int = value.hashCode()
}

actual fun String.toBigDecimal(): BigDecimal = BigDecimal(this)
actual fun Double.toBigDecimal(): BigDecimal = BigDecimal(this)
actual fun Int.toBigDecimal(): BigDecimal = BigDecimal(this)
actual fun Long.toBigDecimal(): BigDecimal = BigDecimal(this)
