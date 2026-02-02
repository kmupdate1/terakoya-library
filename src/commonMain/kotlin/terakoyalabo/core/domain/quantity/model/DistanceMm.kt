package terakoyalabo.core.domain.quantity.model

import terakoyalabo.core.domain.logic.asLowerLimit
import terakoyalabo.core.domain.logic.inclusiveDiscipline
import terakoyalabo.core.domain.primitive.model.ScalarD
import terakoyalabo.core.error.InvalidValidationException
import terakoyalabo.core.error.LawOfTerakoyaException
import terakoyalabo.core.function.validate
import kotlin.jvm.JvmInline

@JvmInline
value class DistanceMm private constructor(val mm: ScalarD) {
    companion object {
        val ZERO = DistanceMm(mm = ScalarD.ZERO)

        @Throws(InvalidValidationException::class, LawOfTerakoyaException::class)
        fun of(rawMm: ScalarD): DistanceMm {
            val validMm = rawMm.validate(
                requirement = ScalarD.ZERO.asLowerLimit.inclusiveDiscipline,
                lazyMessage = { "Distance magnitude must be a non-negative: $it." },
            )

            return DistanceMm(mm = validMm)
        }
    }

    // --- 具象への視点（単位変換） ---
    val cm: ScalarD get() = this.mm / ScalarD.DECA
    val m: ScalarD get() = this.cm / ScalarD.HECTO
    val km: ScalarD get() = this.m / ScalarD.KILO

    // --- 空間の代謝（演算） ---
    @Throws(InvalidValidationException::class)
    operator fun plus(other: DistanceMm): DistanceMm = of(rawMm = this.mm + other.mm)
    @Throws(InvalidValidationException::class)
    operator fun minus(other: DistanceMm): DistanceMm = of(rawMm = this.mm - other.mm)
    /**
     * 倍率計算（距離 × スカラー）
     */
    @Throws(InvalidValidationException::class)
    operator fun times(factor: ScalarD): DistanceMm = of(rawMm = this.mm * factor)
    /**
     * 倍率計算（距離 / スカラー）
     */
    @Throws(InvalidValidationException::class)
    operator fun div(factor: ScalarD): DistanceMm = of(rawMm = this.mm / factor)
}
