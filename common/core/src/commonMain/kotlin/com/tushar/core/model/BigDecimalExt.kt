package com.tushar.core.model

/**
 * Division with scale and rounding mode (expect/actual for platform-specific implementation)
 */
expect fun BigDecimal.divide(
    other: BigDecimal,
    scale: Int,
    roundingMode: RoundingMode
): BigDecimal

/**
 * Rounding mode enum for cross-platform support
 */
expect enum class RoundingMode {
    HALF_UP
}
