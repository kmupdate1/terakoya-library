package terakoyalabo.core.domain.primitive.model

import terakoyalabo.core.error.InvalidValidationException
import terakoyalabo.core.function.validate
import kotlin.jvm.JvmInline

// Rate: 0.0以上の実数を保証する「率」
@JvmInline
value class Rate
@Throws(InvalidValidationException::class)
constructor(val scalar: ScalarD) {
    init {
        scalar.validate(
            requirement = { it.isNegative },
            lazyMessage = { "Rate cannot be negative." },
        )
    }
}
