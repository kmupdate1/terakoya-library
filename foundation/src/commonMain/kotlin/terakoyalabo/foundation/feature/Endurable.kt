package terakoyalabo.foundation.feature

import terakoyalabo.core.domain.identity.model.Identity

interface Endurable<in TContext : Any, out TFeature : Any> {
    val featureId: Identity

    fun endue(context: TContext): TFeature
}
