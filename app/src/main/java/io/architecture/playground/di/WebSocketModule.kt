package io.architecture.playground.di

import android.app.Application
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.messageadapter.gson.GsonMessageAdapter
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.architecture.playground.data.remote.websocket.WebSocketTraceService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WebSocketModule {

    private const val WEBSOCKET_URL = "wss://websockets-diver.glitch.me/nodes/traces"

    @Singleton
    @Provides
    fun provideWebSocketService(scarlet: Scarlet) =
        scarlet.create(WebSocketTraceService::class.java)

    @Singleton
    @Provides
    fun provideScarlet(
        client: OkHttpClient,
        lifecycle: Lifecycle
    ) =
        Scarlet.Builder()
            .webSocketFactory(client.newWebSocketFactory(WEBSOCKET_URL))
//            .lifecycle(lifecycle) // TODO Pass LifecycleService as LifecycleOwner to Scarlet in order to manage ws connection
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