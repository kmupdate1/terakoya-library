package terakoyalabo.core.domain.primitive.model

import terakoyalabo.core.domain.logic.asUpperLimit
import terakoyalabo.core.domain.logic.inclusiveDisciplineBy
import terakoyalabo.core.error.InvalidValidationException
import terakoyalabo.core.function.validate
import kotlin.jvm.JvmInline

// Ratio: Rateを内包し、さらに「1.0以下」を保証する「比」
@JvmInline
value class Ratio private constructor(val rate: Rate) {
    companion object {
        @Throws(InvalidValidationException::class)
        fun of(rate: Rate): Ratio {
            val validRate = rate.validate(
                requirement = { ScalarD.ONE.asUpperLimit.inclusiveDisciplineBy(current = it.scalar) },
                lazyMessage = { "Ratio cannot exceed ${ScalarD.ONE}." }
            )

            return Ratio(rate = validRate)
        }

        // ScalarDから直接作るショートカット
        @Throws(InvalidValidationException::class)
        fun of(scalar: ScalarD): Ratio = of(rate = Rate(scalar = scalar))
    }

    // プロパティとしてRateを公開
    val value: Double get() = rate.scalar.value
}
