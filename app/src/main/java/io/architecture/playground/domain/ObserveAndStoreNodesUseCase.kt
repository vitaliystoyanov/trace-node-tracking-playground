package io.architecture.playground.domain

import android.util.Log
import io.architecture.playground.data.repository.interfaces.NodeRepository
import io.architecture.playground.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ObserveAndStoreNodesUseCase @Inject constructor(
    private var nodesRepository: NodeRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke() = withContext(ioDispatcher) {
        nodesRepository.observeAndStoreNodes()
            .catch { error -> Log.d("SERVICE", "Error - $error") }
            .collect()
    }
}