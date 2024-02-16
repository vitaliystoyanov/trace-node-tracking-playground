package io.architecture.playground.data.repository

import io.architecture.playground.data.local.LocalDataSource
import io.architecture.playground.data.mapping.toExternalAs
import io.architecture.playground.data.mapping.toLocal
import io.architecture.playground.data.remote.NetworkDataSource
import io.architecture.playground.data.repository.interfaces.RouteRepository
import io.architecture.playground.di.DefaultDispatcher
import io.architecture.playground.di.IoDispatcher
import io.architecture.playground.model.Route
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultRouteRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val localTraceRouteDataSource: LocalDataSource,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : RouteRepository {

    override suspend fun add(route: Route) = withContext(ioDispatcher) {
        localTraceRouteDataSource.createOrUpdate(route.toLocal())
    }

    override fun streamAndPersist() =
        networkDataSource.streamRoutes()
            .map { it.toExternalAs() }
            .flowOn(defaultDispatcher)
            .onEach { localTraceRouteDataSource.createOrUpdate(it.toLocal()) }
            .flowOn(ioDispatcher)

    override suspend fun getRouteBy(nodeId: String) = withContext(ioDispatcher) {
        localTraceRouteDataSource.getRouteBy(nodeId)?.toExternalAs()
    }
}