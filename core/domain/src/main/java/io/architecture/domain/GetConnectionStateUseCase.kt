package io.architecture.domain

import io.architecture.common.DefaultDispatcher
import io.architecture.common.IoDispatcher
import io.architecture.data.repository.interfaces.ConnectionStateRepository
import io.architecture.model.Connection
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetConnectionStateUseCase @Inject constructor(
    private var stateRepository: ConnectionStateRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    operator fun invoke(): Flow<Connection> = stateRepository.streamConnectionEvents()
//  TODO      .combine(stateRepository.streamRoundTripTime(100.milliseconds)) { connection: Connection, rtt: UpstreamRtt ->
//            with(connection) { this.rtt = rtt; this }
//}
}