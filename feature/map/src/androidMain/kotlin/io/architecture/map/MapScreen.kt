package io.architecture.map

import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel

@Composable
fun MapScreen(viewModel: MapViewModel = koinViewModel()) {
//    val details by viewModel.detailsUiState.collectAsStateWithLifecycle()
//    val traces by viewModel.tracesUiState.collectAsStateWithLifecycle()
//    val nodesCounter by viewModel.nodeCounterUiState.collectAsStateWithLifecycle()
//    val connectionState by viewModel.connectionsUiState.collectAsStateWithLifecycle()
}
