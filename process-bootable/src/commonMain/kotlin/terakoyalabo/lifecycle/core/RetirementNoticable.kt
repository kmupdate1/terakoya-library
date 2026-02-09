package terakoyalabo.lifecycle.core

import terakoyalabo.core.domain.primitive.model.ScalarL

interface RetirementNoticable {
    fun onRetire(timeoutMillis: ScalarL): Result<Unit>
}
