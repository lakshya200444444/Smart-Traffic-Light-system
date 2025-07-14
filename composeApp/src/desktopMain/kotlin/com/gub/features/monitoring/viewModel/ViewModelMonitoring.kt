package com.gub.features.monitoring.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gub.core.domain.Response
import com.gub.domain.models.monitoring.ModelLiveSignal
import com.gub.features.dashboard.di.DashboardModule
import com.gub.features.monitoring.domain.usecase.UseCaseLiveSignal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ViewModelMonitoring(
    private val liveSignal: UseCaseLiveSignal = DashboardModule.getUseCaseLiveSignal
) : ViewModel() {

    private val _uiState = MutableStateFlow(MonitoringUiState())
    val uiState: StateFlow<MonitoringUiState> = _uiState.asStateFlow()

    init {
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
}

data class MonitoringUiState(
    val isLoading: Boolean = true,
    val liveSignal: ModelLiveSignal? = null,
    val error: String? = null
)