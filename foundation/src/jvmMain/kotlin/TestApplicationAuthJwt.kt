import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import terakoyalabo.core.domain.identity.model.Identity
import terakoyalabo.foundation.ktor.KtorApplicationFeature

class TestApplicationAuthJwt(
    val name: String? = null,
    override val featureId: Identity = Identity.genV5(name = name ?: "TestAuthJwt"),
    internal val configuration: JWTAuthenticationProvider.Config.() -> Unit = {},
) : KtorApplicationFeature<AuthenticationConfig, Authentication>(
    plugin = Authentication,
    configure = { jwt(featureId.compact, configuration) },
)
