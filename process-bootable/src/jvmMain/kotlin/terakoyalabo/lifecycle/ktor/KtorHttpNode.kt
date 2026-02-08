package terakoyalabo.lifecycle.ktor

import io.ktor.server.application.*
import terakoyalabo.core.domain.identity.model.HeartBeat
import terakoyalabo.core.domain.identity.model.Identity
import terakoyalabo.core.domain.logic.sl
import terakoyalabo.lifecycle.node.NodeStatus
import terakoyalabo.lifecycle.node.ServiceNode
import terakoyalabo.lifecycle.node.StatusPublishable

abstract class KtorHttpNode : ServiceNode {
    private var node: NodeStatus = NodeStatus()

    fun bind(application: Application) {
        application.monitor.apply {
            subscribe(ApplicationStarting) {
                verify().onSuccess {
                    node = node.copy(
                        statusId = Identity.gen(),
                        status = StatusPublishable.NodeStatus.ALIVE,
                        timestamp = HeartBeat.now(),
                        message = "Application launched successfully",
                    )
                }.onFailure {
                    node = node.copy(
                        statusId = Identity.gen(),
                        status = StatusPublishable.NodeStatus.DEAD,
                        timestamp = HeartBeat.now(),
                        message = "Application launched failure: ${it.message}",
                    )
                }
            }
            subscribe(ApplicationStopping) {
                onRetire(10_000.sl).onSuccess {
                    node = node.copy(
                        statusId = Identity.gen(),
                        status = StatusPublishable.NodeStatus.RETIRING,
                        timestamp = HeartBeat.now(),
                        message = "Application stopped successfully",
                    )
                }
            }
            subscribe(ApplicationStopped) {
                release().onSuccess {
                    node = node.copy(
                        statusId = Identity.gen(),
                        status = StatusPublishable.NodeStatus.DEAD,
                        timestamp = HeartBeat.now(),
                        message = "Application killed successfully",
                    )
                }
            }
        }
    }

    override fun publish(): StatusPublishable.NodeStatus = node.status
}
