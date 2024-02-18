package io.architecture.data.mapping

import io.architecture.model.Connection
import io.architecture.model.ConnectionEvent
import io.architecture.model.UpstreamRtt

internal fun ConnectionEvent.toExternalAs(): Connection =
    Connection(
        state = this.toExternalState(),
        rtt = UpstreamRtt(0L)
    )

internal fun ConnectionEvent.toExternalState(): Connection.State = when (this) {
    ConnectionEvent.UNDEFINED -> Connection.State.UNDEFINED
    ConnectionEvent.OPENED -> Connection.State.OPENED
    ConnectionEvent.CLOSED -> Connection.State.CLOSED
    ConnectionEvent.CLOSING -> Connection.State.CLOSING
    ConnectionEvent.FAILED -> Connection.State.FAILED
    ConnectionEvent.MESSAGE_RECEIVED -> Connection.State.MESSAGE_RECEIVED
}

