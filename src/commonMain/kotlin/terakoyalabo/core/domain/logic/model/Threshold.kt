package terakoyalabo.core.domain.logic.model

import terakoyalabo.core.domain.primitive.model.ScalarD
import terakoyalabo.core.domain.primitive.model.Signum
import terakoyalabo.core.error.InvalidValidationException
import terakoyalabo.core.error.LawOfTerakoyaException
import terakoyalabo.core.function.validate

data class Threshold
@Throws(InvalidValidationException::class, LawOfTerakoyaException::class)
constructor(val limit: ScalarD, val forbiddenSignum: Signum, val epsilon: ScalarD = ScalarD.ZERO) {
    companion object {
        @Throws(InvalidValidationException::class)
        fun upper(limit: ScalarD): Threshold = Threshold(limit = limit, forbiddenSignum = Signum.POSITIVE)
        @Throws(InvalidValidationException::class)
        fun lower(limit: ScalarD): Threshold = Threshold(limit = limit, forbiddenSignum = Signum.NEGATIVE)
    }

    /**
     * 開区間(a, b)
     */
    @Throws(LawOfTerakoyaException::class)
    fun isViolatedBy(current: ScalarD): Boolean = when (forbiddenSignum) {
        Signum.POSITIVE -> current > limit + epsilon
        Signum.NEGATIVE -> current < limit - epsilon
        Signum.NEUTRAL -> throw LawOfTerakoyaException(
            message = "The laws of Terakoya have been corrupted: Threshold detected an impossible ${Signum.NEUTRAL} state.",
        )
    }

    /**
     * 閉区間[a, b]
     */
    @Throws(LawOfTerakoyaException::class)
    fun isIncludesAndViolatedBy(current: ScalarD): Boolean = when (forbiddenSignum) {
        Signum.POSITIVE -> current >= limit + epsilon
        Signum.NEGATIVE -> current <= limit - epsilon
        Signum.NEUTRAL -> throw LawOfTerakoyaException(
            message = "The laws of Terakoya have been corrupted: Threshold detected an impossible ${Signum.NEUTRAL} state.",
        )
    }

    init {
        limit.validate(
            requirement = { it >= ScalarD.ZERO }, // 拡張使うと循環でオーバーフローの原因、使用しない。
            lazyMessage = { "Threshold limit must be non-negative: $it. Size cannot be negative." },
        )

        forbiddenSignum.validate(
            requirement = { it != Signum.NEUTRAL },
            lazyMessage = {
                "Forbidden Signum cannot be NEUTRAL." +
                        " A threshold must block a specific direction (${Signum.POSITIVE} or ${Signum.NEGATIVE})."
            },
        )
    }
}
