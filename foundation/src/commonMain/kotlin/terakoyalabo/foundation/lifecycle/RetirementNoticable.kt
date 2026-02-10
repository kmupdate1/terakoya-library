package terakoyalabo.foundation.lifecycle

import terakoyalabo.core.domain.primitive.model.ScalarL

interface RetirementNoticable {
    fun onRetire(timeoutMillis: ScalarL): Result<Unit>
}
