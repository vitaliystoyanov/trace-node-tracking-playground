package io.architecture.data

import io.architecture.data.repository.DefaultConnectionStateRepository
import io.architecture.data.repository.DefaultRouteRepository
import io.architecture.data.repository.DefaultTraceRepository
import io.architecture.data.repository.interfaces.ConnectionStateRepository
import io.architecture.data.repository.interfaces.NodeRepository
import io.architecture.data.repository.interfaces.RouteRepository
import io.architecture.data.repository.interfaces.TraceRepository
import io.architecture.datasource.api.LocalDataSource
import io.architecture.datasource.api.NetworkDataSource
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    single {
        DefaultNodeRepository(
            get<LocalDataSource>(),
            get(named("ioDispatcher"))
        )
    } bind NodeRepository::class

    single {
        DefaultConnectionStateRepository(
            get(named("applicationScope")),
            get(named("ioDispatcher")),
            get<NetworkDataSource>()
        )
    } bind ConnectionStateRepository::class

    single {
        DefaultRouteRepository(
            get<NetworkDataSource>(),
            get<LocalDataSource>(),
            get(named("defaultDispatcher")),
            get(named("ioDispatcher")),
        )
    } bind RouteRepository::class

    single {
        DefaultTraceRepository(
            get<NodeRepository>(),
            get<NetworkDataSource>(),
            get<LocalDataSource>(),
            get(named("applicationScope")),
            get(named("defaultDispatcher")),
            get(named("ioDispatcher")),
        )
    } bind TraceRepository::class
}