package terakoyalabotest.library.feature

import io.ktor.server.plugins.contentnegotiation.*
import terakoyalabo.core.domain.identity.model.Identity
import terakoyalabo.foundation.ktor.KtorRouteFeature

class TestRouteContentNegotiation(
    override val featureId: Identity = Identity.genV4(),
    private val configuration: ContentNegotiationConfig.() -> Unit = {},
) : KtorRouteFeature<ContentNegotiationConfig>(
    plugin = ContentNegotiation,
    configure = configuration,
)
