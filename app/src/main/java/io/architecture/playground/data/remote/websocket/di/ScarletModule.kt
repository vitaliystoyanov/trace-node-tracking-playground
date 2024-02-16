package io.architecture.playground.data.remote.websocket.di

import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.messageadapter.protobuf.ProtobufMessageAdapter
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.architecture.playground.data.remote.websocket.scarlet.internal.CustomStreamAdapter
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

//    @Provides Commented.
//    @Singleton
//    fun provideTraceService(@NodeTracesScarlet scarlet: Scarlet) =
//        scarlet.create(ScarletTraceService::class.java)
//
//    @Provides
//    @Singleton
//    fun provideRouteService(@NodeRoutesScarlet scarlet: Scarlet) =
//        scarlet.create(ScarletRouteService::class.java)

    @Provides
    @NodeTracesScarlet
    @Singleton
    fun provideNodeTracesScarlet( // TODO Hilt map multiBinding
        client: OkHttpClient
    ) =
        Scarlet.Builder()
            .webSocketFactory(client.newWebSocketFactory(NODE_TRACES_WS_URL))
            .addMessageAdapterFactory(ProtobufMessageAdapter.Factory())
            .addStreamAdapterFactory(CustomStreamAdapter.Factory)
            .build()

    @Provides
    @NodeRoutesScarlet
    @Singleton
    fun provideNodeRoutesScarlet(
        client: OkHttpClient
    ) =
        Scarlet.Builder()
            .webSocketFactory(client.newWebSocketFactory(NODE_ROUTES_WS_URL))
            .addMessageAdapterFactory(ProtobufMessageAdapter.Factory())
            .addStreamAdapterFactory(CustomStreamAdapter.Factory) // TODO buffer capacity
            .build()


    @Provides
    fun provideOkhttp() =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .build()

}

//@Module
//@InstallIn(SingletonComponent::class)
//abstract class ScarletBindingsModule {
//
//    @Binds
//    @Singleton
//    abstract fun bindTraceService(service: ScarletTraceService): TraceService
//
//    @Binds
//    @Singleton
//    abstract fun bindRoutesService(service: ScarletRouteService): RouteService
//}