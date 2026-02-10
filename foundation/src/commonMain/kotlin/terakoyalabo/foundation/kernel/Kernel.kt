package terakoyalabo.foundation.kernel

import terakoyalabo.core.domain.identity.model.HeartBeat
import terakoyalabo.core.domain.identity.model.Identity

data class Kernel(
    val kernelId: Identity = Identity.genV4(),
    val statusId: Identity = Identity.genV4(),
    val status: StatusPublishable.NodeStatus = StatusPublishable.NodeStatus.STARTING,
    val timestamp: HeartBeat = HeartBeat.now(),
    val message: String? = "Node loaded successfully",
)
