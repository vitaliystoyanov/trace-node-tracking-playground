package io.architecture.playground.feature.map

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.architecture.playground.data.DiverTraceRepository
import io.architecture.playground.model.DiverTrace
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@Stable
interface DiverUiState {
    val trace: DiverTrace
    val historyTraces: List<DiverTrace>
}

class MutableDiverUiState : DiverUiState {
    override var trace: DiverTrace by mutableStateOf(DiverTrace()) // diver trace from websockets data in real-time
    override var historyTraces: List<DiverTrace> by mutableStateOf(ArrayList()) // locally from Room database
}

@HiltViewModel
class MapViewModel @Inject constructor(
    private val diversRepository: DiverTraceRepository
) : ViewModel() {

    // State production pipeline: Stream APIs as sources of state change
    val uiState: StateFlow<DiverUiState> = diversRepository.getStreamDiverTraces()
        .map {
            val state = MutableDiverUiState()
            state.trace = it
            return@map state
        }
        .combine(diversRepository.getStreamDiverTraceHistory()) { state, history ->
            state.historyTraces = history.toMutableList()
            return@combine state
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MutableDiverUiState()
        )
}