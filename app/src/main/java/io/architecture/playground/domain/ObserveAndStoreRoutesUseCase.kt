package io.architecture.playground.domain

import android.util.Log
import io.architecture.playground.data.repository.interfaces.RouteRepository
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

    // TODO  This pattern makes your app more scalable,
    // TODO as classes calling suspend functions don't have to worry about what Dispatcher
    // TODO to use for what type of work. This responsibility lies in the class that does the work
    suspend operator fun invoke() = withContext(ioDispatcher) {
        routesRepository.streamAndPersist()
            .catch { error ->
                Log.e(
                    "REPOSITORY_DEBUG",
                    "Error ",
                    error
                )
            }// TODO Catching of throwables
            .collect()
    }
}