package io.architecture.playground.feature.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.architecture.playground.R
import io.architecture.playground.model.Node
import io.architecture.playground.model.Trace


@Composable
fun NodeBottomSheetContent(
    entry: Map.Entry<Node, Trace>,
    selectedNode: String
) { // TODO With map collection please :)
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        listOf(
            buildAnnotatedString {
                append(
                    String.format(
                        "%s: %f° (reference plane is true north)",
                        entry.value.direction,
                        entry.value.azimuth,
                    )
                )
            },
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Mode: ")
                }
                withStyle(style = SpanStyle(color = colorResource(id = R.color.teal_700))) {
                    append(entry.key.mode.toString())
                }
            },
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Attitude: ")
                }
                append(entry.value.alt.toString())
            },
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Speed: ")
                }
                append("${entry.value.speed} m/s")
            },
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Longitude: ")
                }
                append(entry.value.lon.toString())
            },
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Latitude: ")
                }
                append(entry.value.lat.toString())
            },
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Sent at: ")
                }
                append(entry.value.formattedDatetime)
            },
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Node ID: ")
                }
                append(selectedNode)
            }
        ).forEach { text ->
            Text(
                text = text,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun NodeBottomSheetContentEmpty() {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "No data",
            fontSize = 16.sp
        )
    }
}
