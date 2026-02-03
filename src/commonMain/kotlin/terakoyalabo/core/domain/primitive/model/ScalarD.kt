package terakoyalabo.core.domain.primitive.model

import terakoyalabo.core.error.InvalidValidationException
import terakoyalabo.core.function.validate
import kotlin.jvm.JvmInline
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToLong

@JvmInline
value class ScalarD private constructor(val value: Double) : Scalable<ScalarD> {
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

    // --- 物理的実体への変換（責任範囲の追加） ---
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
    @Throws(InvalidValidationException::class)
    override fun invert(): ScalarD = of(raw = -this.value)

    /**
     * 最も近い整数へ丸めて Long に変換する。
     * 「個体数」や「円」などの不連続な具象へ変換する際の標準ルート。
     * @throws InvalidValidationException
     */
    @Throws(InvalidValidationException::class)
    fun toWholeNumber(): ScalarL = ScalarL.of(raw = value.roundToLong())

    /**
     * 指定された精度で丸めた新しい Scalar を返す。
     * 表示用や、特定の精度を求める計算の途中で使用。
     * @throws InvalidValidationException 理の外の値（NaN/Inf）が生成される場合に送出
     */
    @Throws(InvalidValidationException::class)
    fun round(precision: ScalarD = ZERO): ScalarD {
        val factor = DECA.pow(precision)
        return of(raw = (this * factor / factor).value)
    }

    /**
     * @throws InvalidValidationException 理の外の値（NaN/Inf）が生成される場合に送出
     */
    @Throws(InvalidValidationException::class)
    fun approximatedRound(precision: ScalarD = ZERO): ScalarD {
        val factor = DECA.pow(precision)
        return of(raw = (this * factor).toWholeNumber().value / factor.value)
    }

    /**
     * 冪乗を計算する。
     * @throws InvalidValidationException 理の外の値（NaN/Inf）が生成される場合に送出
     */
    @Throws(InvalidValidationException::class)
    fun pow(exponent: ScalarL): ScalarD {
        if (this.isNegative) throw InvalidValidationException("Imaginary number is not supported.")
        if (exponent.isNegative) throw InvalidValidationException("Negative exponent is not supported for ScalarL.")
        if (this.isZero && exponent.isZero) return ONE
        if (this.isZero) return ZERO

        var res = ONE
        var base = this
        var exp = exponent.value

        while (exp > 0) {
            if (exp % 2 == ScalarL.ONE.value) res *= base // 自前の times を呼ぶ
            if (exp > 1) base *= base       // 自前の times を呼ぶ
            exp /= 2
        }

        return res
    }

    /**
     * 冪乗を計算する。（根号を許容）
     * @throws InvalidValidationException 理の外の値（NaN/Inf）が生成される場合に送出
     */
    @Throws(InvalidValidationException::class)
    fun pow(exponent: ScalarD): ScalarD {
        if (this.isNegative) throw InvalidValidationException("Imaginary number is not supported.")
        return of(raw = this.value.pow(exponent.value))
    }

    override fun toString(): String = value.toString()

    // --- 代謝（演算） ---
    @Throws(InvalidValidationException::class)
    override operator fun plus(other: ScalarD): ScalarD = of(raw = this.value + other.value)
    @Throws(InvalidValidationException::class)
    override operator fun minus(other: ScalarD): ScalarD = of(raw = this.value - other.value)
    @Throws(InvalidValidationException::class)
    override operator fun times(other: ScalarD): ScalarD = of(raw = this.value * other.value)
    @Throws(InvalidValidationException::class)
    override operator fun div(other: ScalarD): ScalarD = of(raw = this.value / other.value)
    override operator fun compareTo(other: ScalarD): Int = this.value.compareTo(other.value)
}
