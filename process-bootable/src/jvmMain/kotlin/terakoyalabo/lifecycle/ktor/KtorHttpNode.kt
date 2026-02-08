package terakoyalabo.lifecycle.ktor

import io.ktor.server.application.*
import terakoyalabo.lifecycle.domain.ServiceNode

abstract class KtorHttpNode : ServiceNode {
    fun bind(application: Application) {
        application.environment.monitor.apply {
            subscribe(ApplicationStarting) {
                verify().onFailure {  }
            }
            subscribe(ApplicationStopping) {
                onRetire(10_000).onFailure {  }
            }
            subscribe(ApplicationStopped) {

            }
        }
    }
}
