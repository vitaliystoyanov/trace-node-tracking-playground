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

@Composable
fun StatusBar(modifier: Modifier, connection: Connection, nodesCount: Int) {
    val systemUiController = rememberSystemUiController()
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")

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
        when (connection.state) {
            Connection.State.UNDEFINED -> R.color.black
            Connection.State.OPENED -> R.color.teal_700
            Connection.State.CLOSED -> R.color.teal_red
            Connection.State.CLOSING -> R.color.teal_700
            Connection.State.FAILED -> R.color.teal_red
            Connection.State.MESSAGE_RECEIVED -> R.color.teal_700
            else -> {
                R.color.black
            }
        }
    )

    val colorOnState = if (connection.state != Connection.State.OPENED) bgColor else color
    systemUiController.setStatusBarColor(colorOnState)
    Row(
        modifier = modifier
            .drawBehind {
                drawRect(colorOnState)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val stateTraces = when (connection.state) {
            Connection.State.OPENED -> "opened"
            Connection.State.MESSAGE_RECEIVED -> "active"
            Connection.State.CLOSING -> "closing..."
            Connection.State.CLOSED -> "closed"
            Connection.State.FAILED -> "FAILED!"
            Connection.State.UNDEFINED -> "initializing..."
        }

        val statusText = "Nodes: $nodesCount | Connection: $stateTraces | " +
                "RTT: ${connection.rtt.value} ms"
        Text(
            text = statusText,
            color = colorResource(id = R.color.white),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
        )
    }
}