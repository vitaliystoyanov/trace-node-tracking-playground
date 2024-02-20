package io.architecture.domain

import android.util.Log
import io.architecture.common.DefaultDispatcher
import io.architecture.common.IoDispatcher
import io.architecture.data.repository.interfaces.ConnectionStateRepository
import io.architecture.data.repository.toExternal
import io.architecture.model.Connection
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class GetConnectionStateUseCase @Inject constructor(
    private var stateRepository: ConnectionStateRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    operator fun invoke(): Flow<Connection> =
        stateRepository.streamConnectionEvents()
            .filterNot { it.toExternal().state == Connection.State.MESSAGE_RECEIVED }
            .onEach { Log.d("RTT_NETWORK", "Connection -> $it") }
            .map { it.toExternal() }
}