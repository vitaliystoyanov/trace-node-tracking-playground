package io.architecture.playground.data.remote.websocket.ktor.internal

import android.util.Log
import io.architecture.playground.data.remote.model.ConnectionEvent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.ws
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.readBytes
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

open class KtorProtobufClient<S : Any, R : Any> {

    private lateinit var _sendClass: KClass<S>
    private lateinit var _receiveClass: KClass<R>
    private val _client: HttpClient
    private val _host: String
    private var _port: Int = 0
    private val _path: String

    companion object {
        inline fun <reified S : Any, reified R : Any> create(
            client: HttpClient,
            host: String,
            port: Int,
            path: String
        ) = KtorProtobufClient(S::class, R::class, client, host, port, path)
    }

    private constructor(
        client: HttpClient,
        host: String,
        port: Int,
        path: String
    ) {
        _client = client
        _host = host
        _port = port
        _path = path
    }

    constructor(
        sendClass: KClass<S>,
        receiveClass: KClass<R>,
        client: HttpClient,
        host: String,
        port: Int,
        path: String
    ) : this(client, host, port, path) {
        _sendClass = sendClass
        _receiveClass = receiveClass
    }

    private var _clientJob: Job? = null
    private var _isSessionOpened = false

    @OptIn(DelicateCoroutinesApi::class)
    private val _connectionEventsShared = MutableSharedFlow<ConnectionEvent>()
    private var _sendShared = MutableSharedFlow<S>()
    private val _receiveShared = MutableSharedFlow<R>()

    @OptIn(DelicateCoroutinesApi::class)
    val connectionEventsShared = _connectionEventsShared
        .shareIn(
            scope = GlobalScope, started = SharingStarted.WhileSubscribed(), replay = 1
        )
    val sendShared: MutableSharedFlow<S> = _sendShared

    val receiveShared = _receiveShared.asSharedFlow()

    open fun isSessionOpened() = _isSessionOpened

    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
    open fun openSession() {
        if (!_isSessionOpened) {
            Log.d(
                "KTOR_SERVICE",
                "openSession() -> Default [ws(s):/$_host:$_port$_path] websocket session is OPENED"
            )
            _clientJob = _client.launch {
                _client.ws(
                    method = HttpMethod.Get,
                    host = _host,
                    port = _port,
                    path = _path
                ) {
                    _isSessionOpened = true
                    produceOutgoing()
                    consumeIncoming()
                }
            }
        } else {
            Log.d(
                "KTOR_SERVICE",
                "openSession() -> Skipped. Default [ws(s)://$_host:$_port$_path] websocket session is is ALREADY OPENED!"
            )
        }
    }

    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
    private fun DefaultClientWebSocketSession.produceOutgoing() {
        launch {
            _sendShared.collect {
                Log.d(
                    "KTOR_SERVICE",
                    "produceOutgoing() -> [ws(s)://$_host:$_port$_path] -> sending -> $it"
                )
                launch {
                    outgoing.send(
                        Frame.Binary(
                            fin = false, // TODO Look up details...
                            ProtoBuf.encodeToByteArray(_sendClass.serializer(), it)
                        )
                    )
                }
            }
        }
    }

    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
    private suspend fun DefaultClientWebSocketSession.consumeIncoming() {
        incoming
            .consumeAsFlow()
            .onStart { _connectionEventsShared.emit(ConnectionEvent.OPENED) }
            .onEach { _connectionEventsShared.emit(ConnectionEvent.MESSAGE_RECEIVED) }
            .onCompletion { _connectionEventsShared.emit(ConnectionEvent.CLOSED) }

            .mapNotNull { it as? Frame.Binary }
            .map {
                ProtoBuf.decodeFromByteArray(
                    _receiveClass.serializer(),
                    it.readBytes()
                )
            }

            .catch {
                Log.e(
                    "KTOR_SERVICE",
                    "consumeIncoming() -> Default [ws(s):/$_host:$_port$_path] websocket session -> an error occurred -> ",
                    it
                )
                _connectionEventsShared.emit(ConnectionEvent.FAILED)
            }
            .collect { _receiveShared.emit(it) }
    }

    open fun closeSession() {
        _isSessionOpened = false
        _clientJob?.cancel()
    }
}