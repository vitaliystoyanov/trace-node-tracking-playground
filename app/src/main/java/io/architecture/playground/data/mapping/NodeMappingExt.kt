package io.architecture.playground.data.mapping

import com.tinder.scarlet.WebSocket
import io.architecture.playground.data.local.model.NodeEntity
import io.architecture.playground.data.remote.model.ConnectionState
import io.architecture.playground.data.remote.model.NetworkNode
import io.architecture.playground.data.remote.model.SocketConnectionState
import io.architecture.playground.model.Node
import io.architecture.playground.model.NodeMode
import java.util.Date

fun WebSocket.Event.toExternal(): ConnectionState = when (this) {
    is WebSocket.Event.OnConnectionOpened<*> -> ConnectionState(SocketConnectionState.OPENED)
    is WebSocket.Event.OnConnectionClosed -> ConnectionState(SocketConnectionState.CLOSED)
    is WebSocket.Event.OnConnectionClosing -> ConnectionState(SocketConnectionState.CLOSING)
    is WebSocket.Event.OnConnectionFailed -> ConnectionState(SocketConnectionState.FAILED)
    is WebSocket.Event.OnMessageReceived -> ConnectionState(SocketConnectionState.MESSAGE_RECEIVED)
}

fun Node.toLocal() = NodeEntity(
    nodeId = nodeId,
    lon = lon,
    time = time,
    speed = speed,
    azimuth = azimuth,
    alt = alt,
    lat = lat,
    mode = mode.valueInt
)

fun NodeEntity.toExternal() = Node(
    nodeId = nodeId,
    lon = lon,
    time = time,
    speed = speed,
    azimuth = azimuth,
    alt = alt,
    lat = lat,
    mode = NodeMode.entries.first { it.valueInt == mode }
)

fun NetworkNode.toLocal() = NodeEntity(
    nodeId = nodeId,
    lon = lon,
    time = Date(time),
    speed = speed,
    azimuth = azimuth,
    alt = alt,
    lat = lat,
    mode = mode
)

fun NodeEntity.toNetwork() = NetworkNode(
    nodeId = nodeId,
    lon = lon,
    time = time.time,
    speed = speed,
    azimuth = azimuth,
    alt = alt,
    lat = lat,
    mode = mode
)

fun Node.toExternal(source: NodeEntity, destination: Node): Node {
    destination.nodeId = source.nodeId
    destination.lon = source.lon
    destination.time = source.time
    destination.speed = source.speed
    destination.azimuth = source.azimuth
    destination.alt = source.alt
    destination.lat = source.lat
    destination.mode = NodeMode.entries.first { it.valueInt == source.mode }
    return destination
}

fun Node.toNetwork() = toLocal().toNetwork()

fun NetworkNode.toExternal() = toLocal().toExternal()

fun List<NodeEntity>.toNetwork() = map(NodeEntity::toNetwork)

fun List<Node>.toLocal() = map(Node::toLocal)

@JvmName("localToExternal")
fun List<NodeEntity>.toExternal() = map(NodeEntity::toExternal)

@JvmName("networkToLocal")
fun List<NetworkNode>.toLocal() = map(NetworkNode::toLocal)

@JvmName("externalToNetwork")
fun List<Node>.toNetwork() = map(Node::toNetwork)

@JvmName("networkToExternal")
fun List<NetworkNode>.toExternal() = map(NetworkNode::toExternal)