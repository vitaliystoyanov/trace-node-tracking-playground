package io.architecture.playground.data.mapping

import com.tinder.scarlet.WebSocket
import io.architecture.playground.data.local.LocalNode
import io.architecture.playground.data.remote.model.ConnectionState
import io.architecture.playground.data.remote.model.NetworkNode
import io.architecture.playground.data.remote.model.SocketConnectionState
import io.architecture.playground.model.NodeMode
import io.architecture.playground.model.Node

fun WebSocket.Event.toExternal(): ConnectionState = when (this) {
    is WebSocket.Event.OnConnectionOpened<*> -> ConnectionState(SocketConnectionState.OPENED)
    is WebSocket.Event.OnConnectionClosed -> ConnectionState(SocketConnectionState.CLOSED)
    is WebSocket.Event.OnConnectionClosing -> ConnectionState(SocketConnectionState.CLOSING)
    is WebSocket.Event.OnConnectionFailed -> ConnectionState(SocketConnectionState.FAILED)
    is WebSocket.Event.OnMessageReceived -> ConnectionState(SocketConnectionState.MESSAGE_RECEIVED)
}

fun Node.toLocal() = LocalNode(
    nodeId = nodeId,
    lon = lon,
    time = time,
    speed = speed,
    azimuth = azimuth,
    alt = alt,
    lat = lat,
    mode = mode.valueInt
)

fun LocalNode.toExternal() = Node(
    nodeId = nodeId,
    lon = lon,
    time = time,
    speed = speed,
    azimuth = azimuth,
    alt = alt,
    lat = lat,
    mode = NodeMode.entries.first { it.valueInt == mode }
)

fun NetworkNode.toLocal() = LocalNode(
    nodeId = nodeId,
    lon = lon,
    time = time,
    speed = speed,
    azimuth = azimuth,
    alt = alt,
    lat = lat,
    mode = mode
)

fun LocalNode.toNetwork() = NetworkNode(
    nodeId = nodeId,
    lon = lon,
    time = time,
    speed = speed,
    azimuth = azimuth,
    alt = alt,
    lat = lat,
    mode = mode
)

fun Node.toNetwork() = toLocal().toNetwork()

fun NetworkNode.toExternal() = toLocal().toExternal()

fun List<LocalNode>.toNetwork() = map(LocalNode::toNetwork)

fun List<Node>.toLocal() = map(Node::toLocal)

@JvmName("localToExternal")
fun List<LocalNode>.toExternal() = map(LocalNode::toExternal)

@JvmName("networkToLocal")
fun List<NetworkNode>.toLocal() = map(NetworkNode::toLocal)

@JvmName("externalToNetwork")
fun List<Node>.toNetwork() = map(Node::toNetwork)

@JvmName("networkToExternal")
fun List<NetworkNode>.toExternal() = map(NetworkNode::toExternal)