package io.architecture.playground.data.repository

import io.architecture.playground.data.local.LocalNodeRouteDataSource
import io.architecture.playground.data.mapping.toExternal
import io.architecture.playground.data.mapping.toLocal
import io.architecture.playground.data.remote.interfaces.NetworkDataSource
import io.architecture.playground.data.repository.interfaces.RouteRepository
import io.architecture.playground.model.Route
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class DefaultRouteRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val localNodeRouteDataSource: LocalNodeRouteDataSource
) : RouteRepository {

    override suspend fun add(route: Route) = localNodeRouteDataSource.add(route.toLocal())

    override fun observeAndStoreRoutes(): Flow<Route> =
        networkDataSource.streamRoutes()
            .map { it.toExternal() }
            .onEach { localNodeRouteDataSource.add(it.toLocal()) }

    override suspend fun getRouteBy(nodeId: String) =
        localNodeRouteDataSource.getRouteBy(nodeId)?.toExternal()
}