package terakoyalabo.core.domain.primitive.model

import terakoyalabo.core.error.InvalidValidationException
import terakoyalabo.core.function.validate
import kotlin.jvm.JvmInline
import kotlin.math.abs
import kotlin.math.roundToLong

@JvmInline
value class ScalarD private constructor(val value: Double) : Scalable<ScalarD>, Calculatable<ScalarD> {
    companion object {
        // 演算子の混入は今後検討
        val ZERO: ScalarD = ScalarD(value = 0.0)
        val ONE: ScalarD = ScalarD(value = 1.0)
        val DECA: ScalarD = ScalarD(value = 10.0)
        val HECTO: ScalarD = DECA * DECA    // 10^2
        val KILO: ScalarD = HECTO * DECA    // 10^3
        val MEGA: ScalarD = KILO * KILO     // 10^6
        val GIGA: ScalarD = MEGA * KILO     // 10^9
        val DECI: ScalarD  = ONE / DECA     // 10^-1
        val CENTI: ScalarD = ONE / HECTO    // 10^-2
        val MILLI: ScalarD = ONE / KILO     // 10^-3
        val MICRO: ScalarD = ONE / MEGA     // 10^-6
        val NANO: ScalarD  = ONE / GIGA     // 10^-9
        val SEXA: ScalarD = ScalarD(value = 6.0) * DECA     // 60 times
        val NEGATIVE_ONE: ScalarD = ScalarD(value = -1.0)

        @Throws(InvalidValidationException::class)
        fun of(raw: Double): ScalarD {
            val validRaw = raw
                .validate(requirement = { it.isNaN() }, lazyMessage = { "No definition (NaN)." })
                .validate(requirement = { it.isInfinite() }, lazyMessage = { "No infinite definition (Infinite)." })

            return ScalarD(value = validRaw)
        }
    }

    /**
     * 最も近い整数へ丸めて Long に変換する。
     * 「個体数」や「円」などの不連続な具象へ変換する際の標準ルート。
     * @throws InvalidValidationException
     */
    @Throws(InvalidValidationException::class)
    fun toWholeNumber(): ScalarL = ScalarL.of(raw = value.roundToLong())

    override fun toString(): String = value.toString()

    // --- Scalable 物理的実体への変換（責任範囲の追加） ---
    override val isPositive: Boolean get() = value > ZERO.value
    override val isNegative: Boolean get() = value < ZERO.value
    override val isZero: Boolean get() = value == ZERO.value

    /**
     * 絶対値
     * @throws InvalidValidationException 理の外の値（NaN/Inf）が生成される場合に送出
     */
    override val abs: ScalarD get() = of(raw = abs(x = this.value))

    /**
     * 符号反転
     * @throws InvalidValidationException 理の外の値（NaN/Inf）が生成される場合に送出
     */
    override val inversion: ScalarD get() = of(raw = -this.value)

    // --- Calculatable 代謝（演算） ---
    override val zero: ScalarD get() = ZERO
    override val one: ScalarD get() = ONE
    override operator fun plus(other: ScalarD): ScalarD = of(raw = this.value + other.value)
    override operator fun minus(other: ScalarD): ScalarD = of(raw = this.value - other.value)
    override operator fun times(other: ScalarD): ScalarD = of(raw = this.value * other.value)
    override operator fun div(other: ScalarD): ScalarD = of(raw = this.value / other.value)
    override operator fun compareTo(other: ScalarD): Int = this.value.compareTo(other.value)
}
