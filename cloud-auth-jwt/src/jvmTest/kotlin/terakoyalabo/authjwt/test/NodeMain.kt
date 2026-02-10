package terakoyalabo.authjwt.test

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import terakoyalabo.authjwt.annotation.TerakoyaAuthDsl

@TerakoyaAuthDsl
fun createNode(settings: () -> TerakoyaSettings): KtorServerNode =
    KtorServerNode(settings = settings.invoke())

fun Application.module() {
    install(Authentication) {}
}
