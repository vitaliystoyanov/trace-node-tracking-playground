package io.architecture.playground

import AppWrapper
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.architecture.map.MapScreen
import org.koin.compose.KoinContext

class MapActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) { // TODO Implement Splash screen API + user data fetching
        super.onCreate(savedInstanceState)

        setContent {
            KoinContext {
//                MapScreen()
                AppWrapper()
            }
        }

        Intent(this, NetworkForegroundService::class.java).also {
            it.action = Actions.START.name
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(it)
                return
            }
            startService(it)
        }
    }
}