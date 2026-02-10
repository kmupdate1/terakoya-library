package terakoyalabo.foundation.ktor

import io.ktor.server.application.*
import io.ktor.server.routing.Route
import terakoyalabo.foundation.feature.Endurable

abstract class KtorRouteFeature<TConfiguration : Any>(
    private val plugin: RouteScopedPlugin<TConfiguration>,
    private val configure: TConfiguration.() -> Unit = {},
) : Endurable<Route, PluginInstance> {
    override fun endue(context: Route): PluginInstance = context.install(plugin, configure)
}
