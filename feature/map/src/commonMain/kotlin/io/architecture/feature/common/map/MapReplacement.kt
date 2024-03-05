package io.architecture.feature.common.map

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.architecture.core.design.system.component.AnimatedBlinkText
import io.architecture.core.design.system.theme.red_state_danger
import io.architecture.core.design.system.theme.yellow_green
import io.architecture.model.Trace

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MapReplacement(
    modifier: Modifier = Modifier,
    nodeTraces: Sequence<Trace>,
    onNodeClick: (String) -> Unit,
) {
    Column {
        AnimatedBlinkText(
            modifier = Modifier.padding(vertical = 20.dp).fillMaxWidth(),
            text = "Map not yet implemented. Provided list of latest traces:",
            textAlign = TextAlign.Center,
            color = red_state_danger
        )
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            itemsIndexed(nodeTraces.take(20).toList()) { index, trace ->
                ListItem(modifier = Modifier.requiredHeight(20.dp)) {
                    Text(
                        modifier = Modifier.clickable {
                            onNodeClick(trace.nodeId)
                        }.fillMaxWidth(),
                        color = yellow_green,
                        text = "$index: ${trace.nodeId} [${trace.lon.toString().slice(0..12)}," +
                                "${trace.lat.toString().slice(0..12)}]",
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}