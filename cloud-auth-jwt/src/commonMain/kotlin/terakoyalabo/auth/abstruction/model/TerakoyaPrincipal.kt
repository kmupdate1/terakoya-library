package terakoyalabo.auth.abstruction.model

import terakoyalabo.core.domain.identity.model.HeartBeat
import terakoyalabo.core.domain.identity.model.Identity

data class TerakoyaPrincipal(
    val identity: Identity,
    val affiliation: Affiliation,
    val heartBeat: HeartBeat,
) : Principal
