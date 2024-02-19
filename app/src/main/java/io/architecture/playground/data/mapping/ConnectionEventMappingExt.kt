package io.architecture.playground.data.mapping

import io.architecture.playground.data.remote.model.ConnectionEvent
import io.architecture.playground.model.Connection
import io.architecture.playground.model.UpstreamRtt

fun ConnectionEvent.toExternalAs(): Connection = Connection(
    state = this.toExternalState(),
    rtt = UpstreamRtt(0L)
)

fun ConnectionEvent.toExternalState(): Connection.State = when (this) {
    ConnectionEvent.UNDEFINED -> Connection.State.UNDEFINED
    ConnectionEvent.OPENED -> Connection.State.OPENED
    ConnectionEvent.CLOSED -> Connection.State.CLOSED
    ConnectionEvent.CLOSING -> Connection.State.CLOSING
    ConnectionEvent.FAILED -> Connection.State.FAILED
    ConnectionEvent.MESSAGE_RECEIVED -> Connection.State.MESSAGE_RECEIVED
}

