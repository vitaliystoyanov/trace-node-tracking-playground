package io.architecture.playground

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.architecture.playground.core.pool.PoolManager
import javax.inject.Inject

@HiltAndroidApp
class PlaygroundApplication : Application() {

    @Inject
    lateinit var poolManager: PoolManager

    override fun onCreate() {
        setupCoroutineDebugMode() // TODO Check if it's DEBUG
        super.onCreate()
    }

    private fun setupCoroutineDebugMode() {
        System.setProperty(
            kotlinx.coroutines.DEBUG_PROPERTY_NAME,
            kotlinx.coroutines.DEBUG_PROPERTY_VALUE_ON
        )
    }
}