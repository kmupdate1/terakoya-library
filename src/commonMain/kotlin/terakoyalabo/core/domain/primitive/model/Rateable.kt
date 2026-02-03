package terakoyalabo.core.domain.primitive.model

import terakoyalabo.core.error.InvalidValidationException

interface Rateable {
    val scalar: ScalarD

    @Throws(InvalidValidationException::class)
    fun percent(precision: ScalarD = ScalarD.ONE): ScalarD = (scalar * ScalarD.HECTO).round(precision = precision)
    @Throws(InvalidValidationException::class)
    fun printPercent(precision: ScalarD = ScalarD.ONE): String = "${percent(precision = precision)} %"
}
