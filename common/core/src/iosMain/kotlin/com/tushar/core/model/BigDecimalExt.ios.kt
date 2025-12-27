package com.tushar.core.model

import kotlin.math.pow
import kotlin.math.round

actual fun BigDecimal.divide(
    other: BigDecimal,
    scale: Int,
    roundingMode: RoundingMode
): BigDecimal {
    // Perform division
    val result = this / other

    // Round to specified scale using Double rounding (simplified for iOS)
    val scaleFactor = 10.0.pow(scale)
    val roundedValue = round(result.toDouble() * scaleFactor) / scaleFactor

    return roundedValue.toBigDecimal()
}

actual enum class RoundingMode {
    HALF_UP
}
