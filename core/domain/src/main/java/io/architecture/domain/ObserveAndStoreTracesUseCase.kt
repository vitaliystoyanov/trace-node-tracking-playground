package io.architecture.domain

import android.util.Log
import io.architecture.common.IoDispatcher
import io.architecture.data.repository.interfaces.TraceRepository
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
            .catch { error -> Log.e("REPOSITORY_DEBUG", "Error ", error) }
            .collect()
    }
}