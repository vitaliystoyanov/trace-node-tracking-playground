package io.architecture.playground.di

import android.app.Application
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.messageadapter.gson.GsonMessageAdapter
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.architecture.playground.data.remote.websocket.NodeService
import io.architecture.playground.data.remote.websocket.RoutesService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Qualifier
import javax.inject.Singleton


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class NodeTracesScarlet

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class NodeRoutesScarlet

@Module
@InstallIn(SingletonComponent::class)
object WebSocketModule {

    private const val BASE_URL = "wss://websockets-diver.glitch.me"
    private const val NODE_TRACES_WEBSOCKET_URL = "$BASE_URL/nodes/traces"
    private const val NODE_ROUTES_WEBSOCKET_URL = "$BASE_URL/nodes/routes"

    @Singleton
    @Provides
    fun provideNodeTracesService(@NodeTracesScarlet scarlet: Scarlet) =
        scarlet.create(NodeService::class.java)

    @Singleton
    @Provides
    fun provideNodeRoutesService(@NodeRoutesScarlet scarlet: Scarlet) =
        scarlet.create(RoutesService::class.java)

    @Singleton
    @Provides
    @NodeTracesScarlet
    fun provideNodeTracesScarlet(
        client: OkHttpClient
    ) =
        Scarlet.Builder()
            .webSocketFactory(client.newWebSocketFactory(NODE_TRACES_WEBSOCKET_URL))
            .addMessageAdapterFactory(GsonMessageAdapter.Factory())
            .addStreamAdapterFactory(CoroutinesStreamAdapterFactory())
            .build()

    @Singleton
    @Provides
    @NodeRoutesScarlet
    fun provideNodeRoutesScarlet(
        client: OkHttpClient
    ) =
        Scarlet.Builder()
            .webSocketFactory(client.newWebSocketFactory(NODE_ROUTES_WEBSOCKET_URL))
            .addMessageAdapterFactory(GsonMessageAdapter.Factory())
            .addStreamAdapterFactory(CoroutinesStreamAdapterFactory())
            .build()

    @Provides
    fun provideOkhttp() =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .build()

    @Provides
    fun provideLifeCycle(application: Application) =
        AndroidLifecycle.ofApplicationForeground(application)

}