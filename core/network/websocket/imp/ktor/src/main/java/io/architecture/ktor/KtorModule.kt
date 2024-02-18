package io.architecture.ktor

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.architecture.api.*
import io.architecture.api.model.NetworkClientTime
import io.architecture.api.model.NetworkRoute
import io.architecture.api.model.NetworkServerTime
import io.architecture.api.model.NetworkTrace
import io.architecture.common.ApplicationScope
import io.architecture.common.DefaultDispatcher
import io.architecture.ktor.internal.KtorProtobufClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.protobuf.ProtoBuf
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KtorClientModule {

    @Provides
    @Singleton
    fun provideKtorTraceClient(client: HttpClient) =
        KtorProtobufClient.create<Any, NetworkTrace>(
            client,
            BASE_WS_HOST,
            BASE_WS_PORT,
            NODE_TRACES_WS_PATH
        )

    @Provides
    @Singleton
    fun provideKtorRouteClient(client: HttpClient) =
        KtorProtobufClient.create<Any, NetworkRoute>(
            client,
            BASE_WS_HOST,
            BASE_WS_PORT,
            NODE_ROUTES_WS_PATH
        )

    @Provides
    @Singleton
    fun provideKtorRttClient(client: HttpClient) =
        KtorProtobufClient.create<NetworkClientTime, NetworkServerTime>(
            client,
            BASE_WS_HOST,
            BASE_WS_PORT,
            RTT_WS_PATH
        )

    @Provides
    @Singleton
    fun provideRttService(
        client: KtorProtobufClient<NetworkClientTime, NetworkServerTime>,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
    ): RttService =
        KtorRttService(client, scope, dispatcher)

    @Provides
    @Singleton
    fun provideTraceService(
        client: KtorProtobufClient<Any, NetworkTrace>,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
    ): TraceService =
        KtorTraceService(client, scope, dispatcher)

    @Provides
    @Singleton
    fun provideRoutesService(
        client: KtorProtobufClient<Any, NetworkRoute>,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
    ): RouteService =
        KtorRouteService(client, scope, dispatcher)

    @Provides
    @Singleton
    @OptIn(ExperimentalSerializationApi::class)
    fun provideKtorClient(): HttpClient = HttpClient(CIO) {

        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(ProtoBuf)
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.i("KTOR_SERVICE", message)
                }
            }
            level = LogLevel.ALL
        }
    }
}



