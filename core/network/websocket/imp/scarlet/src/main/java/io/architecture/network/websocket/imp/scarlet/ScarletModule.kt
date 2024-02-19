package io.architecture.network.websocket.imp.scarlet

import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
internal annotation class NodeTracesScarlet

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
internal annotation class NodeRoutesScarlet

//@Module
//@InstallIn(SingletonComponent::class)
//object WebSocketModule {
//
////    @Provides Commented.
////    @Singleton
////    fun provideTraceService(@NodeTracesScarlet scarlet: Scarlet) =
////        scarlet.create(ScarletTraceService::class.java)
////
////    @Provides
////    @Singleton
////    fun provideRouteService(@NodeRoutesScarlet scarlet: Scarlet) =
////        scarlet.create(ScarletRouteService::class.java)
//
//    @Provides
//    @NodeTracesScarlet
//    @Singleton
//    fun provideNodeTracesScarlet( // TODO Hilt map multiBinding
//        client: OkHttpClient
//    ) =
//        Scarlet.Builder()
//            .webSocketFactory(client.newWebSocketFactory(NODE_TRACES_WS_URL))
//            .addMessageAdapterFactory(ProtobufMessageAdapter.Factory())
//            .addStreamAdapterFactory(CustomStreamAdapter.Factory)
//            .build()
//
//    @Provides
//    @NodeRoutesScarlet
//    @Singleton
//    fun provideNodeRoutesScarlet(
//        client: OkHttpClient
//    ) =
//        Scarlet.Builder()
//            .webSocketFactory(client.newWebSocketFactory(NODE_ROUTES_WS_URL))
//            .addMessageAdapterFactory(ProtobufMessageAdapter.Factory())
//            .addStreamAdapterFactory(CustomStreamAdapter.Factory) // TODO buffer capacity
//            .build()
//
//
//    @Provides
//    fun provideOkhttp() =
//        OkHttpClient.Builder()
//            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
//            .build()
//
//}

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