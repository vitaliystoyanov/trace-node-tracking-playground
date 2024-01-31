package io.architecture.playground.feature.diverdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.architecture.playground.data.DiverTraceRepository
import io.architecture.playground.model.DiverTrace
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class DiversUiState(
    val items: List<DiverTrace> = emptyList(),
    val userMessage: Int? = null
)

@HiltViewModel
class DiversViewModel @Inject constructor(
    private val diversRepository: DiverTraceRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(false)
    val uiState: StateFlow<DiversUiState> = TODO()
}