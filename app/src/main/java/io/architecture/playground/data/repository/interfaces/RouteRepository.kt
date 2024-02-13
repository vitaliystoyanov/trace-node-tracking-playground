package io.architecture.playground.data.repository.interfaces

import io.architecture.playground.model.Route
import kotlinx.coroutines.flow.Flow

interface RouteRepository {

    suspend fun add(route: Route)

    fun streamAndPersist(): Flow<Route>

    suspend fun getRouteBy(nodeId: String): Route?

}