package io.architecture.common.ext

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.takeWhile
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

fun <T> Flow<T>.chunked(chunkSize: Int): Flow<List<T>> {
    val buffer = mutableListOf<T>()

    return flow {
        this@chunked.collect {
            buffer.add(it)
            if (buffer.size == chunkSize) {
                emit(buffer.toList())
                buffer.clear()
            }
        }
        if (buffer.isNotEmpty()) {
            emit(buffer.toList())
        }
    }
}

fun <T, R> Flow<T>.chunked(size: Int, transform: suspend (List<T>) -> R): Flow<R> = flow {
    val cache = ArrayList<T>(size)
    collect {
        cache.add(it)
        if (cache.size == size) {
            emit(transform(cache))
            cache.clear()
        }
    }
}

private object Done
private object TimerExpired

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.chunked(
    sizeLimit: Int,
    timeLimit: Duration,
): Flow<List<T>> {
    require(sizeLimit > 0) { "'sizeLimit' must be positive: $sizeLimit" }
    require(timeLimit > 0.milliseconds) { "'timeLimit' must be positive: $timeLimit" }

    val upstream: Flow<Any?> = this

    return flow {
        val timerEnabled = MutableSharedFlow<Boolean?>()
        var queue = mutableListOf<T>()

        merge(
            upstream.onCompletion { emit(Done) },
            timerEnabled
                .takeWhile { it != null }
                .flatMapLatest { enabled ->
                    if (enabled!!)
                        flow {
                            delay(timeLimit)
                            emit(TimerExpired)
                        }
                    else
                        emptyFlow()
                }
        )
            .collect { element ->
                when (element) {
                    Done -> {
                        if (queue.isNotEmpty()) {
                            emit(queue)
                            queue = mutableListOf()
                        }

                        timerEnabled.emit(null)
                    }

                    TimerExpired -> {
                        if (queue.isNotEmpty()) {
                            emit(queue)
                            queue = mutableListOf()
                        }
                    }

                    else -> {
                        @Suppress("UNCHECKED_CAST")
                        queue.add(element as T)

                        if (queue.size >= sizeLimit) {
                            emit(queue)
                            queue = mutableListOf()
                            timerEnabled.emit(false)
                        } else if (queue.size == 1)
                            timerEnabled.emit(true)
                    }
                }
            }
    }
}