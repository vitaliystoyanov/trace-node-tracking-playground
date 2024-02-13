package io.architecture.playground.feature.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun NodeBottomSheetContent(
    state: DetailsUiState,
    selectedNode: String
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        listOf( // TODO With map please :)
            buildAnnotatedString {
                append(
                    String.format(
                        "%s: %f° (reference plane is true north)",
                        state.lastTrace?.direction,
                        state.lastTrace?.azimuth,
                    )
                )
            },
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Attitude: ")
                }
                append(state.lastTrace?.alt.toString())
            },
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Speed: ")
                }
                append("${state.lastTrace?.speed} m/s")
            },
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Longitude: ")
                }
                append(state.lastTrace?.lon.toString())
            },
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Latitude: ")
                }
                append(state.lastTrace?.lat.toString())
            },
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Sent at: ")
                }
                append(state.lastTrace?.formattedDatetime)
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
