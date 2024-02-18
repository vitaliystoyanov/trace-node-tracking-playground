package io.architecture.api

import io.architecture.api.model.NetworkRoute
import kotlinx.coroutines.flow.Flow


interface RouteService {
    fun streamRoutes(): Flow<NetworkRoute>
}