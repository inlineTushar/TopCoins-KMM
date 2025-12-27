package com.tushar.core.model

/**
 * Expect declaration for BigDecimal to support multiplatform.
 * Android/JVM uses java.math.BigDecimal, iOS uses NSDecimalNumber wrapper.
 */
expect class BigDecimal {
    constructor(value: String)
    constructor(value: Double)
    constructor(value: Int)
    constructor(value: Long)

    companion object {
        val ZERO: BigDecimal
        val ONE: BigDecimal
    }

    fun toDouble(): Double
    fun toPlainString(): String
    override fun toString(): String

    operator fun plus(other: BigDecimal): BigDecimal
    operator fun minus(other: BigDecimal): BigDecimal
    operator fun times(other: BigDecimal): BigDecimal
    operator fun div(other: BigDecimal): BigDecimal
    operator fun compareTo(other: BigDecimal): Int
}

/**
 * Factory functions for creating BigDecimal instances
 */
expect fun String.toBigDecimal(): BigDecimal
expect fun Double.toBigDecimal(): BigDecimal
expect fun Int.toBigDecimal(): BigDecimal
expect fun Long.toBigDecimal(): BigDecimal
