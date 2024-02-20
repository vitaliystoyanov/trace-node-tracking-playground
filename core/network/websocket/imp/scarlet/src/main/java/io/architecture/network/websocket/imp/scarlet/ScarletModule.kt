package io.architecture.network.websocket.imp.scarlet

import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.architecture.network.websocket.api.NODE_ROUTES_WS_URL
import io.architecture.network.websocket.api.NODE_TRACES_WS_URL
import io.architecture.network.websocket.imp.scarlet.internal.CustomGsonMessageAdapter
import io.architecture.network.websocket.imp.scarlet.internal.CustomStreamAdapter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Qualifier
import javax.inject.Singleton


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
internal annotation class NodeTracesScarlet

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
internal annotation class NodeRoutesScarlet

@Module
@InstallIn(SingletonComponent::class)
object ScarletWebSocketModule {

    @Provides
    @Singleton
    fun provideTraceService(@NodeTracesScarlet scarlet: Scarlet) =
        scarlet.create(ScarletTraceService::class.java)

    @Provides
    @Singleton
    fun provideRouteService(@NodeRoutesScarlet scarlet: Scarlet) =
        scarlet.create(ScarletRouteService::class.java)

    @Provides
    @NodeTracesScarlet
    @Singleton
    fun provideNodeTracesScarlet(client: OkHttpClient) =
        Scarlet.Builder()
            .webSocketFactory(client.newWebSocketFactory(NODE_TRACES_WS_URL))
            .addMessageAdapterFactory(CustomGsonMessageAdapter.Factory())
            .addStreamAdapterFactory(CustomStreamAdapter.Factory)
            .build()

    @Provides
    @NodeRoutesScarlet
    @Singleton
    fun provideNodeRoutesScarlet(client: OkHttpClient) =
        Scarlet.Builder()
            .webSocketFactory(client.newWebSocketFactory(NODE_ROUTES_WS_URL))
            .webSocketFactory(client.newWebSocketFactory(NODE_TRACES_WS_URL))
            .addMessageAdapterFactory(CustomGsonMessageAdapter.Factory())
            .addStreamAdapterFactory(CustomStreamAdapter.Factory)
            .build()


    @Provides
    fun provideOkhttp() =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .build()
}