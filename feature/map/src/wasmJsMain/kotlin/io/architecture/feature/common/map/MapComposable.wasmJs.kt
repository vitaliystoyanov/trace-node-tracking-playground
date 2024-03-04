package io.architecture.feature.common.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.architecture.core.design.system.theme.black
import io.architecture.model.Route
import io.architecture.model.Trace

@Composable
actual fun MapComposable(
    padding: PaddingValues,
    nodeTraces: Sequence<Trace>,
    displayRoute: Route?,
    onNodeClick: (String) -> Unit,
) {
    Box(modifier = Modifier.background(black)) {
        MapReplacement(Modifier.padding(6.dp), nodeTraces, onNodeClick)
    }
}