package terakoyalabo.core.domain.quantity.model

import terakoyalabo.core.domain.logic.*
import terakoyalabo.core.domain.logic.model.Delta
import terakoyalabo.core.domain.primitive.model.ScalarD
import terakoyalabo.core.error.InvalidValidationException
import terakoyalabo.core.error.LawOfTerakoyaException
import terakoyalabo.core.function.validate
import kotlin.jvm.JvmInline

@JvmInline
value class Celsius private constructor(val degree: ScalarD) {
    companion object {
        /** 絶対零度 (-273.15℃) */
        val ABSOLUTE_ZERO: Celsius = Celsius(degree = ScalarD.of(raw = -273.15))

        @Throws(InvalidValidationException::class, LawOfTerakoyaException::class)
        fun of(rawDegree: ScalarD): Celsius {
            // 絶対零度を下回ることは「理」が許さない
            val validDegree = rawDegree.validate(
                requirement = ABSOLUTE_ZERO.degree.asLowerLimit.inclusiveDiscipline,
                lazyMessage = { "Temperature below absolute zero is physically impossible: $it℃." },
            )
            return Celsius(degree = validDegree)
        }
    }

    // --- 生命・自然の境界線（感じの良い定数） ---
    /**
     * @throws LawOfTerakoyaException
     */
    val isFreezing: Boolean get() = ScalarD.ZERO.asUpperLimit.inclusiveDisciplineBy(current = this.degree)
    /**
     * @throws LawOfTerakoyaException
     */
    val isBoiling: Boolean get() = ScalarD.HECTO.asLowerLimit.inclusiveDisciplineBy(current = this.degree)
    val isNormal: Boolean get() = 25.0.sVal.asTargetWithEpsilon(epsilon = 5.0.sVal).isReachedBy(current = this.degree)

    fun toAbsoluteDegree(): ScalarD = (this.degree - ABSOLUTE_ZERO.degree).abs

    // --- 空間の代謝（演算） ---
    // 温度同士の足し算は物理的に特殊（混合）なため、
    // 「温度差」の概念（Delta）を導入するのも寺子屋風ですが、まずはシンプルに。
    @Throws(InvalidValidationException::class)
    operator fun plus(delta: Delta): Celsius = of(rawDegree = this.degree + delta.magnitude)
    @Throws(InvalidValidationException::class)
    operator fun minus(delta: Delta): Celsius = of(rawDegree = this.degree - delta.magnitude)
}
