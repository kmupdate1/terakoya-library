package terakoyalabo.foundation.ktor

import io.ktor.server.application.*
import terakoyalabo.foundation.feature.Endurable

abstract class KtorApplicationFeature<TPlugin : Any, TConfiguration : Any>(
    private val plugin: Plugin<Application, TPlugin, TConfiguration>,
    private val configure: TPlugin.() -> Unit = {},
) : Endurable<Application, TConfiguration> {
    override fun endue(context: Application): TConfiguration = context.install(plugin, configure)
}
