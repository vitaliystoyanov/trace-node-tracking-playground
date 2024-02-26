package io.architecture.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.architecture.model.Trace


@Composable
fun NodeDetailsContent(
    trace: Trace?,
    selectedNode: String,
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        listOf( // TODO With map please :)
            buildAnnotatedString {
                append(
                    "${trace?.direction}: ${trace?.azimuth} (reference plane is true north)"
                )
            },
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Attitude: ")
                }
                append(trace?.alt.toString())
            },
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Speed: ")
                }
                append("${trace?.speed} m/s")
            },
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Longitude: ")
                }
                append(trace?.lon.toString())
            },
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Latitude: ")
                }
                append(trace?.lat.toString())
            },
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Sent at: ")
                }
                append(trace?.formattedDatetime)
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
