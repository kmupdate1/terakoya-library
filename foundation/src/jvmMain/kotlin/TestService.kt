import terakoyalabo.foundation.ktor.KernelConfiguration
import terakoyalabo.foundation.ktor.KtorEmbApplicationKernel

class TestService : KtorEmbApplicationKernel(
    configuration = KernelConfiguration()
) {
    fun launch() {
        // 1. 具象化したサービスを生成
        val service = object : KtorEmbApplicationKernel(
            configuration = KernelConfiguration(port = 8080)
        ) {
            // 必要に応じてライフサイクルメソッドを調整
        }

        val kernel = Builder(kernel = service)
            .applyApp(endurable = TestApplicationAuthJwt())
            .applyRoute(endurable = TestRouteContentNegotiation())
            .build()

        kernel.run()
            .onSuccess { println("サーバは現在、8080ポートで生存しています。Ctrl+Cで停止してください。") }
            .onFailure { println("サーバの起動に失敗しました。[${it.message}]") }


        // テストプロセスが死なないように、スレッドを眠らせ続ける
        // Thread.currentThread().join()
    }
}

fun main() {
    TestService().launch()
}
