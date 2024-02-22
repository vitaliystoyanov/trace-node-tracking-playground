package io.architecture.domain

import android.util.Log
import io.architecture.data.repository.interfaces.TraceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

class ObserveAndStoreTracesUseCase(
    private var tracesRepository: TraceRepository,
    private val ioDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke() = withContext(ioDispatcher) {
        tracesRepository.streamAndPersist()
            .catch { error -> Log.e("REPOSITORY_DEBUG", "Error ", error) }
            .collect()
    }
}