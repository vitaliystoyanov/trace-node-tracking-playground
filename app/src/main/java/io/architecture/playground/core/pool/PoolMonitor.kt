package io.architecture.playground.core.pool

import android.util.Log
import io.architecture.playground.core.logCoroutineInfo
import io.architecture.playground.di.ApplicationScope
import io.architecture.playground.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PoolMonitor @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @ApplicationScope private val applicationScope: CoroutineScope,
) {

    private var job: Job? = null

    fun start(map: PoolMap) {
        job = applicationScope.launch(defaultDispatcher + CoroutineName("POOL_OBJECTS_MONITOR")) {
            while (true) {
                Log.d("POOL_OBJECTS_MONITOR", "=".repeat(50))
                logCoroutineInfo("POOL_OBJECTS_MONITOR", "")
                Log.d("POOL_OBJECTS_MONITOR", "=".repeat(50))
                map.entries.forEach { (memberClass, pool) ->
                    Log.d(
                        "POOL_OBJECTS_MONITOR",
                        ": $pool[${memberClass.simpleName}] where [maxSize=${pool.getMaxSize()}]"
                    )
                }
                delay(MONITOR_PERIOD)
            }
        }
    }

    fun stop() {
        Log.d(
            "POOL_OBJECTS_MONITOR",
            ": Stopping pool objects monitor..."
        )
        this.job?.cancel()
    }

    companion object {
        const val MONITOR_PERIOD: Long = 5_000
    }
}