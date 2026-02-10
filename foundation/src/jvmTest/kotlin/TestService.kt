import terakoyalabo.foundation.ktor.KernelConfiguration
import terakoyalabo.foundation.ktor.KtorApplicationKernel
import terakoyalabotest.library.feature.TestApplicationAuthJwt
import terakoyalabotest.library.feature.TestRouteContentNegotiation

class TestService : KtorApplicationKernel(
    configuration = KernelConfiguration()
) {
    private val kernel = Builder(kernel = this)
        .apply(endurable = TestApplicationAuthJwt())
        .apply(endurable = TestRouteContentNegotiation())
        .build()
        .run()
}
