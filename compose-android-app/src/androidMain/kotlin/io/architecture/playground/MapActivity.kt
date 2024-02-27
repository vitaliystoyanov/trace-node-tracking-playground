package io.architecture.playground

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.architecture.map.MapScreen

class MapActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) { // TODO Implement Splash screen API + user data fetching
        super.onCreate(savedInstanceState)

        setContent {
            MapScreen()
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