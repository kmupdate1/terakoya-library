package terakoyalabo.foundation.ktor

import io.ktor.server.application.*
import terakoyalabo.core.domain.identity.model.HeartBeat
import terakoyalabo.core.domain.identity.model.Identity
import terakoyalabo.core.domain.logic.sl
import terakoyalabo.foundation.kernel.StatusPublishable
import terakoyalabo.foundation.kernel.Kernel
import terakoyalabo.foundation.kernel.ServiceKernel

abstract class KtorHttpKernelLifecycle : ServiceKernel {
    fun bind(application: Application) {
        application.monitor.apply {
            subscribe(ApplicationStarting) {
                onEndue().onSuccess {
                    kernel = kernel.copy(
                        statusId = Identity.genV4(),
                        status = StatusPublishable.NodeStatus.ALIVE,
                        timestamp = HeartBeat.now(),
                        message = "Node endued successfully",
                    )
                }.onFailure {
                    kernel = kernel.copy(
                        statusId = Identity.genV4(),
                        status = StatusPublishable.NodeStatus.ALIVE,
                        timestamp = HeartBeat.now(),
                        message = "Node endue failed: ${it.message}",
                    )

                    throw it
                }

                onVerify().onSuccess {
                    kernel = kernel.copy(
                        statusId = Identity.genV4(),
                        status = StatusPublishable.NodeStatus.ALIVE,
                        timestamp = HeartBeat.now(),
                        message = "Node launched successfully",
                    )
                }.onFailure {
                    kernel = kernel.copy(
                        statusId = Identity.genV4(),
                        status = StatusPublishable.NodeStatus.DEAD,
                        timestamp = HeartBeat.now(),
                        message = "Node launched failed: ${it.message}",
                    )

                    throw it
                }

                onLaunch().onSuccess {

                }.onFailure {
                    throw it
                }
            }
            subscribe(ApplicationStopping) {
                onRetire(10_000.sl).onSuccess {
                    kernel = kernel.copy(
                        statusId = Identity.genV4(),
                        status = StatusPublishable.NodeStatus.RETIRING,
                        timestamp = HeartBeat.now(),
                        message = "Node stopped successfully",
                    )
                }
            }
            subscribe(ApplicationStopped) {
                onRelease().onSuccess {
                    kernel = kernel.copy(
                        statusId = Identity.genV4(),
                        status = StatusPublishable.NodeStatus.DEAD,
                        timestamp = HeartBeat.now(),
                        message = "Node killed successfully",
                    )
                }
            }
        }
    }

    override fun publish(): StatusPublishable.NodeStatus = kernel.status

    private var kernel: Kernel = Kernel()
}
