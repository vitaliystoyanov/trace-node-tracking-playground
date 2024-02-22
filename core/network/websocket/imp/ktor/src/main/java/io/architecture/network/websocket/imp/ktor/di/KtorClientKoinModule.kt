package io.architecture.network.websocket.imp.ktor.di

import io.architecture.network.websocket.api.BASE_WS_HOST
import io.architecture.network.websocket.api.BASE_WS_PORT
import io.architecture.network.websocket.api.NODE_ROUTES_WS_PATH
import io.architecture.network.websocket.api.NODE_TRACES_WS_PATH
import io.architecture.network.websocket.api.RTT_WS_PATH
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
            BASE_WS_HOST,
            BASE_WS_PORT,
            NODE_TRACES_WS_PATH
        )
    }
    single(named("ktorRouteClient")) {
        KtorProtobufClient.create(
            Any::class,
            NetworkRoute::class,
            get<HttpClient>(),
            BASE_WS_HOST,
            BASE_WS_PORT,
            NODE_ROUTES_WS_PATH
        )
    }
    single(named("ktorRttClient")) {
        KtorProtobufClient.create(
            NetworkClientTime::class,
            NetworkServerTime::class,
            get<HttpClient>(),
            BASE_WS_HOST,
            BASE_WS_PORT,
            RTT_WS_PATH
        )
    }
}