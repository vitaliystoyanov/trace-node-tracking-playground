package io.architecture.feature.common.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.architecture.model.Connection
import io.architecture.model.Trace
import io.architecture.ui.NodeDetailsContent
import io.architecture.ui.StatusBar
import kotlinx.coroutines.flow.StateFlow
import org.koin.compose.koinInject

@Composable
fun MapScreen() {
    val viewModel = koinInject<MapViewModel>()

    // https://developer.android.com/jetpack/compose/side-effects?hl=en#remembercoroutinescope
    // rememberCoroutineScope is a composable function that returns a CoroutineScope bound
    // to the point of the Composition where it's called.
    // The scope will be cancelled when the call leaves the Composition.
    val scope = rememberCoroutineScope()

    with(viewModel) {
        val connectionsUiState = connectionsUiState
        val nodeCounterUiState: StateFlow<Int> = nodeCounterUiState
        val tracesUiState: StateFlow<Sequence<Trace>> = tracesUiState

        RootContent(tracesUiState, nodeCounterUiState, connectionsUiState)
    }
}

@Composable
fun RootContent(
    tracesUiState: StateFlow<Sequence<Trace>>,
    nodeCounterUiState: StateFlow<Int>,
    connectionsUiState: StateFlow<Connection>,
) {
    val viewModel = koinInject<MapViewModel>() // TODO ??? Danger
    val detailsUiState: StateFlow<NodeDetailsUiState> = viewModel.detailsUiState

    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedNodeId by remember { mutableStateOf("") }

    @OptIn(ExperimentalMaterial3Api::class)
    val sheetState = rememberModalBottomSheetState()

    Scaffold(modifier = Modifier.background(Color.Black)) { contentPadding ->
        MapComposable(
            contentPadding,
            tracesUiState.value,
            null, // TODO
            onNodeClick = { nodeId ->
                selectedNodeId = nodeId
                viewModel.loadDetails(nodeId)
                showBottomSheet = true
            }
        )
        StatusBar(
            modifier = Modifier
                .wrapContentSize()
                .fillMaxWidth()
                .height(20.dp),
            connectionsUiState.collectAsState().value,
            tracesUiState.collectAsState().value.toList().size
        )
    }

    if (showBottomSheet) {
        @OptIn(ExperimentalMaterial3Api::class)
        ModalBottomSheet(
            onDismissRequest = {
                selectedNodeId = ""
                showBottomSheet = false
                viewModel.clearDetails()
            },
            sheetState = sheetState
        ) {
            NodeDetailsContent(detailsUiState.value.lastTrace, selectedNodeId)
        }
    }
}