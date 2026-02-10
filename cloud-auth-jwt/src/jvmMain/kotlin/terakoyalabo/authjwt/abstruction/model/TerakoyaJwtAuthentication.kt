package terakoyalabo.authjwt.abstruction.model

import io.ktor.server.auth.jwt.*
import terakoyalabo.authjwt.domain.principal.model.Affiliation
import terakoyalabo.core.domain.identity.model.HeartBeat
import terakoyalabo.core.domain.identity.model.Identity
import terakoyalabo.authjwt.core.TerakoyaJwtPrincipal
import terakoyalabo.authjwt.domain.authenticator.TerakoyaJwtAuthenticator

class TerakoyaJwtAuthentication : TerakoyaJwtAuthenticator<JWTCredential, TerakoyaJwtPrincipal?> {
    override suspend fun authenticate(credential: JWTCredential): TerakoyaJwtPrincipal? {
        val payload = credential.payload
        val sub = payload.subject ?: return null

        val role = payload.getClaim("role").asString() ?: Affiliation.GUEST.name

        return try {
            TerakoyaJwtPrincipal(
                identity = Identity.genV4(),
                affiliation = Affiliation.valueOf(value = role),
                heartBeat = HeartBeat.now(),
            )
        } catch (_: Exception) { null }
    }
}
