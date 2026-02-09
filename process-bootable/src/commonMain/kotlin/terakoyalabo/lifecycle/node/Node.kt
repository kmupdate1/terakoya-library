package terakoyalabo.lifecycle.node

import terakoyalabo.core.domain.identity.model.HeartBeat
import terakoyalabo.core.domain.identity.model.Identity
import terakoyalabo.lifecycle.core.StatusPublishable.NodeStatus

data class Node(
    val nodeId: Identity = Identity.gen(),
    val statusId: Identity = Identity.gen(),
    val status: NodeStatus = NodeStatus.STARTING,
    val timestamp: HeartBeat = HeartBeat.now(),
    val message: String? = "Application loaded successfully",
)
