package com.tushar.domain.model

import java.math.RoundingMode as JavaRoundingMode

actual fun BigDecimal.divide(
    other: BigDecimal,
    scale: Int,
    roundingMode: RoundingMode
): BigDecimal = BigDecimal(value.divide(other.value, scale, JavaRoundingMode.HALF_UP))

actual typealias RoundingMode = JavaRoundingMode
