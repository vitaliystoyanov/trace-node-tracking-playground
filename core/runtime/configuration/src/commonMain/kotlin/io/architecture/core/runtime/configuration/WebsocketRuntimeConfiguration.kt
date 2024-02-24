package io.architecture.core.runtime.configuration

data class WebsocketRuntimeConfiguration(
    val BASE_WS_HOST: String = "192.168.1.197",
    val BASE_WS_PORT: Int = 8080,
    val BASE_WS_URL: String = "ws://$BASE_WS_HOST}:$BASE_WS_PORT",

    // Scarlet uses:
    val NODE_TRACES_WS_URL: String = "$BASE_WS_URL/nodes/traces",
    val NODE_ROUTES_WS_URL: String = "$BASE_WS_URL/nodes/routes",

    // Ktor uses:
    val NODE_TRACES_WS_PATH: String = "/nodes/traces",
    val NODE_ROUTES_WS_PATH: String = "/nodes/routes",
    val RTT_WS_PATH: String = "/rtt",
)
