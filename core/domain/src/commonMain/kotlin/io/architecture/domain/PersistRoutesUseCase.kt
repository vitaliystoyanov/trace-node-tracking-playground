package io.architecture.domain

import io.architecture.data.repository.interfaces.RouteRepository
import io.architecture.runtime.logging.Logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class PersistRoutesUseCase(
    private var routeRepository: RouteRepository,
    private val defaultDispatcher: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher,
) {

    operator fun invoke(): Flow<Unit> = routeRepository
        .streamRoutes(isPersisted = true)
        .flowOn(ioDispatcher)
        .map { }
        .catch { error -> Logger.error("REPOSITORY_DEBUG", "PersistRoutesUseCase: ", error) }
}