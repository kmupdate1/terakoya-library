package terakoyalabo.core.domain.primitive.model

import terakoyalabo.core.error.InvalidValidationException
import terakoyalabo.core.error.LawOfTerakoyaException
import kotlin.jvm.JvmInline
import kotlin.math.abs

@JvmInline
value class ScalarL private constructor(val value: Long) : Scalable<ScalarL> {
    companion object {
        val ZERO: ScalarL = ScalarL(value = 0L)
        val ONE = ScalarL(value = 1L)
        val NEGATIVE = ScalarL(-1L)

        @Throws(InvalidValidationException::class)
        fun of(raw: Long): ScalarL = ScalarL(value = raw)
    }

    override val isPositive: Boolean get() = value > 0L
    override val isNegative: Boolean get() = value < 0L
    override val isZero: Boolean get() = value == 0L

    /**
     * @throws InvalidValidationException
     */
    override val abs: ScalarL get() = if (value == Long.MIN_VALUE) {
        throw LawOfTerakoyaException("Overflow in absolute value.")
    } else of(raw = abs(value))

    @Throws(InvalidValidationException::class)
    override fun invert(): ScalarL = if (value == Long.MIN_VALUE) {
        throw LawOfTerakoyaException("Overflow in inversion: Long.MIN_VALUE cannot be inverted.")
    } else of(raw = -value)

    @Throws(InvalidValidationException::class, LawOfTerakoyaException::class)
    fun pow(exponent: ScalarL): ScalarL {
        if (this.isNegative) throw InvalidValidationException("Imaginary number is not supported.")
        if (exponent.isNegative) throw InvalidValidationException("Negative exponent is not supported for ScalarL.")
        if (this.isZero && exponent.isZero) return ONE
        if (this.isZero) return ZERO

        var res = ONE
        var base = this
        var exp = exponent.value

        while (exp > 0) {
            if (exp % 2 == ONE.value) res *= base // 自前の times を呼ぶ
            if (exp > 1) base *= base       // 自前の times を呼ぶ
            exp /= 2
        }

        return res
    }

    override fun toString(): String = value.toString()

    @Throws(InvalidValidationException::class, LawOfTerakoyaException::class)
    override operator fun plus(other: ScalarL): ScalarL {
        val res = this.value + other.value
        // 加算の理：正＋正＝負、または 負＋負＝正 になったらオーバーフロー
        if (((this.value xor res) and (other.value xor res)) < ZERO.value) {
            throw LawOfTerakoyaException("Long overflow in addition: $value + ${other.value}")
        }
        return of(raw = res)
    }
    @Throws(InvalidValidationException::class, LawOfTerakoyaException::class)
    override operator fun minus(other: ScalarL): ScalarL {
        val res = this.value - other.value
        // 減算の理：符号が異なるもの同士を引いて、結果の符号が引かれる数と異なればオーバーフロー
        if (((this.value xor other.value) and (this.value xor res)) < ZERO.value) {
            throw LawOfTerakoyaException("Long overflow in subtraction: $value - ${other.value}")
        }
        return of(raw = res)
    }
    @Throws(InvalidValidationException::class, LawOfTerakoyaException::class)
    override operator fun times(other: ScalarL): ScalarL {
        if (other.value == ZERO.value) return ZERO
        val res = this.value * other.value
        // 乗算の理：結果を片方で割って元に戻らなければオーバーフロー
        if (res / other.value != this.value) {
            throw LawOfTerakoyaException("Long overflow in multiplication: $value * ${other.value}")
        }
        return of(raw = res)
    }
    @Throws(InvalidValidationException::class, LawOfTerakoyaException::class)
    override operator fun div(other: ScalarL): ScalarL {
        if (other.isZero) throw LawOfTerakoyaException("Division by zero.")
        // Long.MIN_VALUE / -1 だけはオーバーフローする唯一の除算
        if (this.value == Long.MIN_VALUE && other.value == NEGATIVE.value) {
            throw LawOfTerakoyaException("Long overflow in division.")
        }
        return of(raw = this.value / other.value)
    }
    override operator fun compareTo(other: ScalarL): Int = value.compareTo(other.value)
}
