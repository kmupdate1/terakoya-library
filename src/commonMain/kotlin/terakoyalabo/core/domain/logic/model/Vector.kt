package terakoyalabo.core.domain.logic.model

import terakoyalabo.core.domain.logic.asLowerLimit
import terakoyalabo.core.domain.logic.discipline
import terakoyalabo.core.domain.primitive.model.ScalarD
import terakoyalabo.core.domain.primitive.model.Signum
import terakoyalabo.core.error.InvalidValidationException
import terakoyalabo.core.error.LawOfTerakoyaException
import terakoyalabo.core.function.validate

data class Vector
@Throws(InvalidValidationException::class, LawOfTerakoyaException::class)
constructor(val magnitude: ScalarD, val direction: Signum) {
    companion object {
        val STATIONARY: Vector = Vector(magnitude = ScalarD.ZERO, direction = Signum.NEUTRAL)

        @Throws(InvalidValidationException::class)
        fun of(rawMag: ScalarD): Vector = rawMag.let { mag ->
            Vector(
                magnitude = mag.abs,
                direction = Signum.of(scalar = mag),
            )
        }
    }

    init {
        magnitude
            .validate(
                requirement = ScalarD.ZERO.asLowerLimit.discipline,
                lazyMessage = { "Magnitude must be positive: $it." },
            )
            .validate(
                requirement = { it != ScalarD.ZERO && direction == Signum.NEUTRAL },
                lazyMessage = {
                    "Logical conflict detected: Magnitude is $it but direction is NEUTRAL. " +
                            "A non-zero magnitude must have an active polarity (POSITIVE or NEGATIVE). " +
                            "Please ensure the magnitude is zero if you intend to represent a balanced/stationary state."
                },
            )
    }
}
