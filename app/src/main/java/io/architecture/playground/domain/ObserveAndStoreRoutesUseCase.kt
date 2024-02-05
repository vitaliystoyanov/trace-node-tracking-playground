package io.architecture.playground.domain

import android.util.Log
import io.architecture.playground.data.repository.interfaces.RouteRepository
import io.architecture.playground.di.DefaultDispatcher
import io.architecture.playground.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ObserveAndStoreRoutesUseCase @Inject constructor(
    private var routesRepository: RouteRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke() = withContext(ioDispatcher) {
        routesRepository.observeAndStoreRoutes()
            .catch { error -> Log.d("SERVICE", "Error - $error") }
            .collect()
    }
}