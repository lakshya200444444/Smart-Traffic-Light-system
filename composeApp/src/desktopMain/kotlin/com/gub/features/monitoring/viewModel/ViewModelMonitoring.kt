package com.gub.features.monitoring.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gub.core.domain.Response
import com.gub.core.logic.SignalingAlgorithm.getNextPhaseBasedOnData
import com.gub.domain.models.monitoring.ModelLiveSignal
import com.gub.features.dashboard.di.DashboardModule
import com.gub.features.monitoring.di.MonitoringModule
import com.gub.features.monitoring.domain.usecase.UseCaseLiveSignal
import com.gub.features.monitoring.presentation.components.TrafficPhase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ViewModelMonitoring(
    private val liveSignal: UseCaseLiveSignal = MonitoringModule.getUseCaseLiveSignal
) : ViewModel() {

    private val _uiState = MutableStateFlow(MonitoringUiState())
    val uiState: StateFlow<MonitoringUiState> = _uiState.asStateFlow()

    init {
        startSignalTimer()
        getLiveTrafficSignal()
    }

    private fun getLiveTrafficSignal() = viewModelScope.launch {
        when(val res =  liveSignal()) {
            is Response.Error -> {}
            Response.Loading -> {}
            is Response.Success -> {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    liveSignal = res.data,
                    error = null
                )
            }
        }
    }

    private fun startSignalTimer() = viewModelScope.launch {
        while (isActive) {
            delay(1000)
            _uiState.update { state ->
                val newTime = state.timeRemaining - 1
                if (newTime > 0) {
                    state.copy(timeRemaining = newTime)
                } else {
                    print( "STATE -> " + state.toString())
                    val nextPhaseWithTime = getNextPhaseBasedOnData(state)

                    println("Next STATE -> ${nextPhaseWithTime.first} Time -> ${nextPhaseWithTime.second}")

                    state.copy(
                        currentPhase = nextPhaseWithTime.first,
                        timeRemaining = nextPhaseWithTime.second
                    )
                }
            }
        }
    }
}

data class MonitoringUiState(
    val isLoading: Boolean = true,
    val timeRemaining: Int = 5,
    val currentPhase: TrafficPhase = TrafficPhase.NS_GREEN,
    val liveSignal: ModelLiveSignal? = null,
    val trafficPhase: TrafficPhase = TrafficPhase.NS_GREEN,
    val error: String? = null
)