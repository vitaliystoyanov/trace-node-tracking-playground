package io.architecture.playground

import android.app.Application
import androidx.multidex.BuildConfig
import io.architecture.core.di.coreKoinModules
import io.architecture.database.imp.room.di.roomDaoModule
import io.architecture.database.imp.room.di.roomDatabaseModule
import io.architecture.map.featureMapModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.lazyModules
import org.koin.core.runOnKoinStarted
import org.koin.dsl.lazyModule
import org.koin.mp.KoinPlatform

class PlaygroundApplication : Application() {

    @OptIn(KoinExperimentalAPI::class)
    override fun onCreate() {
        if (BuildConfig.DEBUG) setupCoroutineDebugMode()

        val modules = lazyModule {
            includes(
                coreKoinModules,
                roomDaoModule,
                roomDatabaseModule,
                featureMapModule
            )
        }

        startKoin {
            androidLogger()
            androidContext(this@PlaygroundApplication)
            lazyModules(modules)
        }

        val koin = KoinPlatform.getKoin()

        koin.runOnKoinStarted { _ ->
            super.onCreate()
        }
    }

    private fun setupCoroutineDebugMode() {
        System.setProperty(
            kotlinx.coroutines.DEBUG_PROPERTY_NAME,
            kotlinx.coroutines.DEBUG_PROPERTY_VALUE_ON
        )
    }
}