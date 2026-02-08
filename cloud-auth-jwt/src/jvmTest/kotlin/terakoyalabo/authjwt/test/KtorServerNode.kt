package terakoyalabo.authjwt.test

import io.ktor.server.cio.CIO
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.applicationEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.util.concurrent.TimeUnit

class KtorServerNode(
    private val settings: TerakoyaSettings,
) : KtorConfiguratable, KtorBootable, KtorUnbootable {
    override fun configure(): ApplicationNode {
        TODO()
    }

    override fun boot() {
        val ktorConfig = MapApplicationConfig(
            "ktor.deployment.port" to settings.port.toString(),
            "ktor.deployment.host" to settings.host,
        )

        val factory = when (settings.soil) {
            NodeSoil.NATIVE -> CIO
            NodeSoil.STABLE -> Netty
        }

        server = embeddedServer(
            factory = factory,
            environment = applicationEnvironment {
                config = ktorConfig
            },
        ).apply { start(wait = true) }
    }

    override fun shutdown(): Result<Unit> =
        server?.stop(1000, 5000, TimeUnit.MILLISECONDS)
            ?.let { Result.success(Unit) }
            ?: Result.failure(IllegalStateException("Server is not started"))

    private var server: EmbeddedServer<*, *>? = null
}

enum class NodeSoil {
    NATIVE, STABLE,
}

data class TerakoyaSettings(
    val port: Int = 8080,
    val host: String = "127.0.0.1",
    val environment: String = "DEV",
    val soil: NodeSoil = NodeSoil.NATIVE
)

fun TerakoyaSettings.toKtorConfig(): ApplicationConfig {
    return MapApplicationConfig(
        "ktor.deployment.port" to port.toString(),
        "ktor.environment" to environment,
        // 必要に応じて、土壌(Soil)に合わせた細かなKtor設定をここで自動生成する
    )
}
