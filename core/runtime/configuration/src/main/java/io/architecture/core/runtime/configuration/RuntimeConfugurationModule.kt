package io.architecture.core.runtime.configuration

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

class WebsocketRuntimeConfiguration {
    val BASE_WS_HOST = "192.168.1.197"
    val BASE_WS_PORT = 8080
    val BASE_WS_URL = "ws://$BASE_WS_HOST}:$BASE_WS_PORT"

    // Scarlet uses:
    val NODE_TRACES_WS_URL = "$BASE_WS_URL/nodes/traces"
    val NODE_ROUTES_WS_URL = "$BASE_WS_URL/nodes/routes"

    // Ktor uses:
    val NODE_TRACES_WS_PATH = "/nodes/traces"
    val NODE_ROUTES_WS_PATH = "/nodes/routes"
    val RTT_WS_PATH = "/rtt"

    override fun toString(): String {
        return BASE_WS_URL
    }
}

@Module
@ComponentScan("io.architecture.network.websocket.api")
class RuntimeConfigurationModule {

    @Single
    fun websocketRuntimeConfiguration() = WebsocketRuntimeConfiguration()
}