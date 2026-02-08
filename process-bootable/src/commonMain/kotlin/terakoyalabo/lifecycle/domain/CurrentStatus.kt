package terakoyalabo.lifecycle.domain

import terakoyalabo.core.domain.identity.model.HeartBeat
import terakoyalabo.core.domain.identity.model.Identity
import terakoyalabo.lifecycle.domain.StatusPublishable.NodeStatus

data class CurrentStatus(
    val nodeId: Identity = Identity.gen(),
    val statusId: Identity = Identity.gen(),
    val status: NodeStatus = NodeStatus.STARTING,
    val timestamp: HeartBeat = HeartBeat.now(),
    val message: String? = "Application loaded successfully",
)
