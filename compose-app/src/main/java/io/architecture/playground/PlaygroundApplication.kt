package io.architecture.playground

import android.app.Application
import android.util.Log
import io.architecture.core.runtime.configuration.RuntimeConfigurationModule
import io.architecture.core.runtime.configuration.WebsocketRuntimeConfiguration
import io.architecture.map.viewModelModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.ksp.generated.module

class PlaygroundApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) setupCoroutineDebugMode()

        startKoin {
            androidLogger()
            androidContext(this@PlaygroundApplication)
            modules(RuntimeConfigurationModule().module, viewModelModule)
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