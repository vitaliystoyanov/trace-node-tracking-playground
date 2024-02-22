package io.architecture.playground

import android.app.Application
import android.util.Log
import io.architecture.core.di.coreKoinModules
import io.architecture.core.runtime.configuration.WebsocketRuntimeConfiguration
import io.architecture.map.featureMapModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class PlaygroundApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) setupCoroutineDebugMode()

        startKoin {
            androidLogger()
            androidContext(this@PlaygroundApplication)
            modules(
                coreKoinModules,
                featureMapModule,
            )
        }

        val runtimeConfig: WebsocketRuntimeConfiguration by inject()
        Log.d("RUNTIME_CONFIG", "onCreate: $runtimeConfig")
    }

    private fun setupCoroutineDebugMode() {
        System.setProperty(
            kotlinx.coroutines.DEBUG_PROPERTY_NAME,
            kotlinx.coroutines.DEBUG_PROPERTY_VALUE_ON
        )
    }
}