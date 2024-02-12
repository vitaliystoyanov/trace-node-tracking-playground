package io.architecture.playground.feature.map

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import io.architecture.playground.R
import io.architecture.playground.model.Connection
import io.architecture.playground.model.ConnectionState

@Composable
fun StatusBar(modifier: Modifier, states: Map<Int, Connection>, nodesCount: Int) {
    val systemUiController = rememberSystemUiController()
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")

    val connection = states[Connection.TRACE_SERVICE_CONNECTION]

    val color by infiniteTransition.animateColor(
        initialValue = colorResource(id = R.color.teal_700),
        targetValue = colorResource(id = R.color.teal_700_dark),
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "color"
    )

    val bgColor = colorResource(
        when (connection?.state) {
            ConnectionState.UNDEFINED -> R.color.black
            ConnectionState.OPENED -> R.color.teal_700
            ConnectionState.CLOSED -> R.color.teal_red
            ConnectionState.CLOSING -> R.color.teal_700
            ConnectionState.FAILED -> R.color.teal_red
            ConnectionState.MESSAGE_RECEIVED -> R.color.teal_700
            else -> {
                R.color.black
            }
        }
    )

    val colorOnState = if (connection?.state != ConnectionState.OPENED) bgColor else color
    systemUiController.setStatusBarColor(colorOnState)
    Row(
        modifier = modifier
            .drawBehind {
                drawRect(colorOnState)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val stateTraces = when (connection?.state) {
            ConnectionState.OPENED -> "opened"
            ConnectionState.MESSAGE_RECEIVED -> "active"
            ConnectionState.CLOSING -> "closing..."
            ConnectionState.CLOSED -> "closed"
            ConnectionState.FAILED -> "FAILED!"
            ConnectionState.UNDEFINED -> "initializing..."
            null -> "initializing..."
            else -> {
                ""
            }
        }

        val statusText = if (connection != null)
            "Nodes: $nodesCount | Connection: $stateTraces | " +
                    "RTT: ${connection.rtt.value} ms" else stateTraces
        Text(
            text = statusText,
            color = colorResource(id = R.color.white),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
        )
    }
}