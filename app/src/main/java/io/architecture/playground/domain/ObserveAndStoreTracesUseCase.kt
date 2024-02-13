package io.architecture.playground.domain

import android.util.Log
import io.architecture.playground.data.repository.interfaces.TraceRepository
import io.architecture.playground.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ObserveAndStoreTracesUseCase @Inject constructor(
    private var tracesRepository: TraceRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke() = withContext(ioDispatcher) {
        tracesRepository.streamAndPersist()
            .catch { error -> Log.e("SERVICE", "Error ", error) }
            .collect()
    }
}