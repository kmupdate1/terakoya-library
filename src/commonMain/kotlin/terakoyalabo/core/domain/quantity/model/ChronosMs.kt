package terakoyalabo.core.domain.quantity.model

import terakoyalabo.core.domain.logic.asLowerLimit
import terakoyalabo.core.domain.logic.inclusiveDiscipline
import terakoyalabo.core.domain.primitive.model.ScalarD
import terakoyalabo.core.error.InvalidValidationException
import terakoyalabo.core.error.LawOfTerakoyaException
import terakoyalabo.core.function.validate
import kotlin.jvm.JvmInline

@JvmInline
value class ChronosMs
@Throws(LawOfTerakoyaException::class)
private constructor(val ms: ScalarD) {
    companion object {
        val SECOND: ChronosMs = of(ms = ScalarD.KILO)
        val MINUTE: ChronosMs = of(ms = ScalarD.KILO * ScalarD.SEXA)
        val HOUR: ChronosMs = of(ms = ScalarD.KILO * ScalarD.SEXA * ScalarD.SEXA)

        @Throws(InvalidValidationException::class, LawOfTerakoyaException::class)
        fun of(ms: ScalarD): ChronosMs {
            val validMs = ms.validate(
                requirement = ScalarD.ZERO.asLowerLimit.inclusiveDiscipline,
                lazyMessage = { "ChronosMs must be non-negative. Input: $it." }
            )

            return ChronosMs(ms = validMs)
        }

        @Throws(InvalidValidationException::class)
        fun fromRaw(rawMs: Double): ChronosMs = of(ms = ScalarD.of(raw = rawMs))
    }

    val s: ScalarD get() = ms / ScalarD.KILO
    val min: ScalarD get() = s / ScalarD.SEXA
    val hour: ScalarD get() = min / ScalarD.SEXA

    override fun toString(): String = "${ms}ms"

    @Throws(InvalidValidationException::class)
    operator fun plus(other: ChronosMs): ChronosMs = ChronosMs(ms = this.ms + other.ms)
    @Throws(InvalidValidationException::class)
    operator fun minus(other: ChronosMs): ChronosMs = ChronosMs(ms = this.ms - other.ms)
    @Throws(InvalidValidationException::class)
    operator fun times(scalarD: ScalarD): ChronosMs = of(ms = this.ms * scalarD)
    @Throws(InvalidValidationException::class)
    operator fun div(scalar: ScalarD): ChronosMs = of(this.ms / scalar)
}
