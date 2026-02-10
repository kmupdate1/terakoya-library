package terakoyalabo.foundation.ktor

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import terakoyalabo.core.domain.primitive.model.ScalarL
import terakoyalabo.foundation.feature.Endurable
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

abstract class KtorApplicationKernel(
    private val configuration: KernelConfiguration,
) : KtorHttpKernelLifecycle() {
    override fun onEndue(): Result<Unit> = runCatching {
        println("機能が登録されました")
        applicationEndurables.forEach { endurable -> endurable.endue(context = server.application) }
        server.application.routing {
            routeEndurables.forEach { endurable -> endurable.endue(context = this) }
        }
    }

    override fun onVerify(): Result<Unit> = runCatching {
        println("ヘルスチェックが終了しました")
    }

    override fun onLaunch(): Result<Unit> = runCatching {
        println("アプリケーションが起動されました")
        server.start(wait = true)
    }

    override fun onRetire(timeoutMillis: ScalarL): Result<Unit> = runCatching {
        println("アプリケーションが稼働を終了しました")
        server.stop(1000L, 1000L, TimeUnit.MILLISECONDS)
    }

    override fun onRelease(): Result<Unit> = runCatching {
        println("アプリケーションがリソースを返却しました。")
        exitProcess(0)
    }

    fun run(): Result<Unit> = runCatching { bind(application = server.application) }

    private val server: EmbeddedServer<*, *> = embeddedServer(
        factory = when (configuration.soil) {
            NodeSoil.STABLE -> Netty
            NodeSoil.NATIVE -> CIO
        },
        environment = applicationEnvironment {
            config = MapApplicationConfig(
                "ktor.deployment.port" to configuration.port.toString(),
                "ktor.deployment.host" to configuration.host,
            )
        },
    )

    private val applicationEndurables = mutableListOf<Endurable<Application, Any>>()
    private val routeEndurables = mutableListOf<Endurable<Route, Any>>()

    /**
     * Endurable（機能）を Pipeline へと「登録」する。
     * * 【仕様：順序の保存】
     * ここで apply された順番が、そのまま Ktor の ApplicationCallPipeline への
     * 登録順（インターセプト順）となる。
     * * 理学的な依存関係（例：認証の後にルーティング）がある場合は、
     * その順序を守って apply すること。
     */
    class Builder(private val kernel: KtorApplicationKernel) {
        private val applicationEndurables = mutableListOf<Endurable<Application, Any>>()
        private val routeEndurables = mutableListOf<Endurable<Route, Any>>()

        fun apply(endurable: Endurable<Application, Any>): Builder {
            applicationEndurables.add(endurable)
            return this
        }

        fun apply(endurable: Endurable<Route, Any>): Builder {
            routeEndurables.add(endurable)
            return this
        }

        fun build(): KtorApplicationKernel = kernel.also {
            it.applicationEndurables.addAll(applicationEndurables)
            it.routeEndurables.addAll(routeEndurables)
        }
    }
}

enum class NodeSoil {
    NATIVE, STABLE,
}

data class KernelConfiguration(
    val port: Int = 8080,
    val host: String = "127.0.0.1",
    val environment: String = "DEV",
    val soil: NodeSoil = NodeSoil.NATIVE
)
