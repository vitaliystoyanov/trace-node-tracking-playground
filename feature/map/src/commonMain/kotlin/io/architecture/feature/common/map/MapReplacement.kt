package io.architecture.feature.common.map

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.architecture.model.Trace

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MapReplacement(nodeTraces: Sequence<Trace>, onNodeClick: (String) -> Unit) {
    LazyColumn(Modifier.fillMaxSize()) {
        itemsIndexed(nodeTraces.take(20).toList()) { index, trace ->
            ListItem(modifier = Modifier.requiredHeight(20.dp)) {
                Text(
                    modifier = Modifier.clickable {
                        onNodeClick(trace.nodeId)
                    },
                    text = "$index: ${trace.sentAtTime} ${trace.nodeId}",
                    fontSize = 12.sp
                )
            }
        }
    }
}