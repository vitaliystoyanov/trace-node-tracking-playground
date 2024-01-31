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
import io.architecture.playground.data.remote.websocket.WebSocketDiverService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WebSocketModule {

    private const val WEBSOCKET_URL = "wss://websockets-diver.glitch.me"

    @Singleton
    @Provides
    fun provideWebSocketService(scarlet: Scarlet) =
        scarlet.create(WebSocketDiverService::class.java)

    @Singleton
    @Provides
    fun provideScarlet(
        client: OkHttpClient,
        lifecycle: Lifecycle
    ) =
        Scarlet.Builder()
            .webSocketFactory(client.newWebSocketFactory(WEBSOCKET_URL))
            .lifecycle(lifecycle)
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