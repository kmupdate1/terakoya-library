import terakoyalabo.core.domain.primitive.model.ScalarL
import terakoyalabo.lifecycle.ktor.KtorHttpNode

class ApplicationTest : KtorHttpNode() {
    /*
    private val node by lazy {
        createNode { TerakoyaSettings() }.also { it.boot() }
    }

    fun stop() {
        node.shutdown().fold(
            onSuccess = { exitProcess(0) },
            onFailure = { "Node is sleeping." },
        )
    }
     */
    override fun verifyResources(): Result<Unit> {
        println("ここでリソースの状態をチェック")
        TODO("Not yet implemented")
    }

    override fun onRetire(timeoutMillis: ScalarL): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun releaseResources(): Result<Unit> {
        TODO("Not yet implemented")
    }
}
