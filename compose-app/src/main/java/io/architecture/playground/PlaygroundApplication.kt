package io.architecture.playground

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import io.architecture.core.runtime.configuration.RuntimeConfigurationModule
import io.architecture.core.runtime.configuration.WebsocketRuntimeConfiguration
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.ksp.generated.module

@HiltAndroidApp
class PlaygroundApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) setupCoroutineDebugMode()
        startKoin {
            androidLogger()
            androidContext(this@PlaygroundApplication)
            modules(RuntimeConfigurationModule().module)
        }
        val runtimeConfig = get<WebsocketRuntimeConfiguration>()
        Log.d("RUNTIME_CONFIG", "onCreate: $runtimeConfig")
    }

    private fun setupCoroutineDebugMode() {
        System.setProperty(
            kotlinx.coroutines.DEBUG_PROPERTY_NAME,
            kotlinx.coroutines.DEBUG_PROPERTY_VALUE_ON
        )
    }
}