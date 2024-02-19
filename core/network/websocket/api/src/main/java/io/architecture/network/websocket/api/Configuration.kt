package io.architecture.network.websocket.api

//@Module
//@InstallIn(SingletonComponent::class)
//object BaseWebsocketModule {
//
//    @Provides
//    @Singleton
//    fun provideEnabledWebsocketClientProvider(): WebsocketClientProvider =
//        WebsocketClientProvider.KTOR_PROTOBUF
//
//}

const val BASE_WS_HOST = "192.168.1.197"
const val BASE_WS_PORT = 8080
const val BASE_WS_URL = "ws://$BASE_WS_HOST}:$BASE_WS_PORT"

// Scarlet uses:
const val NODE_TRACES_WS_URL = "$BASE_WS_URL/nodes/traces"
const val NODE_ROUTES_WS_URL = "$BASE_WS_URL/nodes/routes"

// Ktor uses:
const val NODE_TRACES_WS_PATH = "/nodes/traces"
const val NODE_ROUTES_WS_PATH = "/nodes/routes"
const val RTT_WS_PATH = "/rtt"

enum class WebsocketClientProvider {
    SCARLET_GSON, KTOR_PROTOBUF
}