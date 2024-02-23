package io.architecture.network.websocket.imp.scarlet.di

import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import io.architecture.datasource.api.NetworkDataSource
import io.architecture.network.websocket.api.NODE_ROUTES_WS_URL
import io.architecture.network.websocket.api.NODE_TRACES_WS_URL
import io.architecture.network.websocket.api.RouteService
import io.architecture.network.websocket.api.TraceService
import io.architecture.network.websocket.imp.scarlet.ScarletNetworkDataSource
import io.architecture.network.websocket.imp.scarlet.ScarletRouteService
import io.architecture.network.websocket.imp.scarlet.ScarletTraceService
import io.architecture.network.websocket.imp.scarlet.internal.CustomGsonMessageAdapter
import io.architecture.network.websocket.imp.scarlet.internal.CustomStreamAdapter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val scarletKoinModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .build()
    }

    single {
        Scarlet.Builder()
            .webSocketFactory(get<OkHttpClient>().newWebSocketFactory(NODE_ROUTES_WS_URL))
            .addMessageAdapterFactory(CustomGsonMessageAdapter.Factory())
            .addStreamAdapterFactory(CustomStreamAdapter.Factory)
            .build()
        named("scarletRoutes")
    }
    single {
        Scarlet.Builder()
            .webSocketFactory(get<OkHttpClient>().newWebSocketFactory(NODE_TRACES_WS_URL))
            .addMessageAdapterFactory(CustomGsonMessageAdapter.Factory())
            .addStreamAdapterFactory(CustomStreamAdapter.Factory)
            .build()
        named("scarletTraces")
    }

    single {
        get<Scarlet>(named("scarletRoutes"))
            .create(ScarletRouteService::class.java)
    } bind RouteService::class
    single {
        get<Scarlet>(named("scarletTraces"))
            .create(ScarletTraceService::class.java)
    } bind TraceService::class

    single {
        ScarletNetworkDataSource(
            get<ScarletTraceService>(),
            get<ScarletRouteService>()
        )
    } bind NetworkDataSource::class
}