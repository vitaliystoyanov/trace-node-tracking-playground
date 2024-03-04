package io.architecture.ui

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.architecture.core.design.system.theme.red_state_danger
import io.architecture.core.design.system.theme.yellow_state_warning
import io.architecture.core.design.system.theme.yellow_green
import io.architecture.core.design.system.theme.yellow_green_secondary
import io.architecture.core.design.system.theme.black
import io.architecture.core.design.system.theme.teal_700
import io.architecture.core.design.system.theme.white
import io.architecture.model.Connection

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StatusBar(modifier: Modifier, connection: Connection, nodesCount: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")

    val color by infiniteTransition.animateColor(
        initialValue = yellow_green,
        targetValue = yellow_green_secondary,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "color"
    )

    val bgColor = when (connection.state) {
        Connection.State.UNDEFINED -> black
        Connection.State.OPENED -> yellow_green
        Connection.State.CLOSED -> white
        Connection.State.CLOSING -> yellow_state_warning
        Connection.State.FAILED -> red_state_danger
        Connection.State.MESSAGE_RECEIVED -> teal_700
        else -> {
            black
        }
    }

    val stateTraces = when (connection.state) {
        Connection.State.OPENED -> "opened"
        Connection.State.MESSAGE_RECEIVED -> "active"
        Connection.State.CLOSING -> "closing..."
        Connection.State.CLOSED -> "closed"
        Connection.State.FAILED -> "FAILED!"
        Connection.State.UNDEFINED -> "UNDEFINED..."
    }

    val colorOnState = if (connection.state != Connection.State.OPENED) bgColor else color
    val itemModifier = Modifier.padding(2.dp)
    val itemTextColorStated = if (connection.state != Connection.State.UNDEFINED) black else white
    FlowRow(
        modifier = modifier
            .drawBehind {
                drawRect(colorOnState)
            },
        horizontalArrangement = Arrangement.Center
    ) {
        listOf(
            "|Nodes:$nodesCount|",
            "|Connection:$stateTraces|",
            "|RTT:${connection.rtt.value} ms|"
        ).forEach {
            Text(
                modifier = itemModifier,
                text = it,
                color = itemTextColorStated,
                fontSize = 11.sp,
            )
        }
    }
}