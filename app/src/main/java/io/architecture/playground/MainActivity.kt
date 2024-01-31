package io.architecture.playground

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import io.architecture.playground.data.remote.websocket.WebSocketDiverService
import io.architecture.playground.feature.map.MapScreen
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // TODO
    @Inject lateinit var webSocketService: WebSocketDiverService

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MapScreen()
        }
    }
}