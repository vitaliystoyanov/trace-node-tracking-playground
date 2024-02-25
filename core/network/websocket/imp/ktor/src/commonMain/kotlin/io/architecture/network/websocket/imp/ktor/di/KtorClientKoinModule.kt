package io.architecture.network.websocket.imp.ktor.di

import io.architecture.core.runtime.configuration.WebsocketRuntimeConfiguration
import io.architecture.network.websocket.api.model.NetworkClientTime
import io.architecture.network.websocket.api.model.NetworkRoute
import io.architecture.network.websocket.api.model.NetworkServerTime
import io.architecture.network.websocket.api.model.NetworkTrace
import io.architecture.network.websocket.imp.ktor.internal.KtorProtobufClient
import io.ktor.client.HttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

val ktorClientModule = module {
    single(named("ktorTraceClient")) {
        KtorProtobufClient.create( // https://insert-koin.io/docs/reference/koin-core/definitions#dealing-with-generics
            Any::class,
            NetworkTrace::class,
            get<HttpClient>(),
            get<WebsocketRuntimeConfiguration>().BASE_WS_HOST, // TODO Refactor it :)
            get<WebsocketRuntimeConfiguration>().BASE_WS_PORT,
            get<WebsocketRuntimeConfiguration>().NODE_TRACES_WS_PATH
        )
    }

    single(named("ktorRouteClient")) {
        KtorProtobufClient.create(
            Any::class,
            NetworkRoute::class,
            get<HttpClient>(),
            get<WebsocketRuntimeConfiguration>().BASE_WS_HOST,
            get<WebsocketRuntimeConfiguration>().BASE_WS_PORT,
            get<WebsocketRuntimeConfiguration>().NODE_ROUTES_WS_PATH
        )
    }

    single(named("ktorRttClient")) {
        KtorProtobufClient.create(
            NetworkClientTime::class,
            NetworkServerTime::class,
            get<HttpClient>(),
            get<WebsocketRuntimeConfiguration>().BASE_WS_HOST,
            get<WebsocketRuntimeConfiguration>().BASE_WS_PORT,
            get<WebsocketRuntimeConfiguration>().RTT_WS_PATH
        )
    }
}