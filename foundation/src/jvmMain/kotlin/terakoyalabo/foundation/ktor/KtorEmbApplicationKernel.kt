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

abstract class KtorEmbApplicationKernel(
    private val configuration: KernelConfiguration,
) : KtorEmbHttpKernelLifecycle() {
    override fun onEndue(): Result<Unit> = runCatching {
        println("[1] 機能登録は完了しているため、ここでは実行しません。")
    }

    override fun onVerify(): Result<Unit> = runCatching {
        println("[2] ヘルスチェックが終了しました。")
    }

    override fun onLaunch(): Result<Unit> = runCatching {
        println("[3] アプリケーションが起動されました。")
    }

    override fun onRetire(timeoutMillis: ScalarL): Result<Unit> = runCatching {
        println("[4] アプリケーションが稼働を終了しました。")
    }

    override fun onRelease(): Result<Unit> = runCatching {
        println("[5] アプリケーションがリソースを返却しました。")
        server?.stop(gracePeriodMillis = 1_000L, timeoutMillis = 1_000L)
    // .stop(shutdownGracePeriod = 1_000L, shutdownTimeout = 1_000L, TimeUnit.MILLISECONDS)
    }

    fun run(): Result<Unit> = runCatching {
        server = embeddedServer(
            factory = when (configuration.soil) {
                NodeSoil.STABLE -> Netty
                NodeSoil.NATIVE -> CIO
            },
            port = configuration.port,
            host = configuration.host,
            /*
            configure = {
                workerGroupSize = 4
                connectionGroupSize = 2
                shutdownTimeout = 1_000L
                shutdownGracePeriod = 1_000L
            },
            */
            module = {
                this@KtorEmbApplicationKernel.bind(this)

                applicationEndurables.forEach { endurable -> endurable.endue(context = this) }
                routing {
                    routeEndurables.forEach { endurable -> endurable.endue(context = this) }
                }
            },
        ).start(wait = true)
    }

    private val applicationEndurables = mutableListOf<Endurable<Application, Any>>()
    private val routeEndurables = mutableListOf<Endurable<Route, Any>>()
    private var server: EmbeddedServer<*, *>? = null

    /**
     * Endurable（機能）を Pipeline へと「登録」する。
     * * 【仕様：順序の保存】
     * ここで apply された順番が、そのまま Ktor の ApplicationCallPipeline への
     * 登録順（インターセプト順）となる。
     * * 理学的な依存関係（例：認証の後にルーティング）がある場合は、
     * その順序を守って apply すること。
     */
    class Builder(private val kernel: KtorEmbApplicationKernel) {
        private val applicationEndurables = mutableListOf<Endurable<Application, Any>>()
        private val routeEndurables = mutableListOf<Endurable<Route, Any>>()

        fun applyApp(endurable: Endurable<Application, Any>): Builder {
            applicationEndurables.add(endurable)
            return this
        }

        fun applyRoute(endurable: Endurable<Route, Any>): Builder {
            routeEndurables.add(endurable)
            return this
        }

        fun build(): KtorEmbApplicationKernel = kernel.also {
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
    val host: String = "0.0.0.0",
    val environment: String = "DEV",
    val soil: NodeSoil = NodeSoil.NATIVE
)
