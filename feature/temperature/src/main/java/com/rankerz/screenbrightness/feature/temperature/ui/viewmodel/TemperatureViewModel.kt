package com.rankerz.screenbrightness.feature.temperature.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rankerz.screenbrightness.core.domain.repository.ColorTemperatureRepository // Need this for auto-toggle
import com.rankerz.screenbrightness.feature.temperature.domain.usecase.GetColorTemperatureUseCase
import com.rankerz.screenbrightness.feature.temperature.domain.usecase.SetColorTemperatureUseCase
import com.rankerz.screenbrightness.feature.temperature.ui.TemperatureUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine // Import combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TemperatureViewModel @Inject constructor(
    private val getColorTemperatureUseCase: GetColorTemperatureUseCase,
    private val setColorTemperatureUseCase: SetColorTemperatureUseCase,
    private val colorTemperatureRepository: ColorTemperatureRepository // Inject repository for auto-toggle
) : ViewModel() {

    private val _uiState = MutableStateFlow(TemperatureUiState())
    val uiState: StateFlow<TemperatureUiState> = _uiState.asStateFlow()

    init {
        fetchInitialTemperatureAndAutoState()
    }

    private fun fetchInitialTemperatureAndAutoState() {
        viewModelScope.launch {
            combine(
                getColorTemperatureUseCase(),
                colorTemperatureRepository.getAutoTemperatureEnabled() // Observe auto-toggle state
            ) { temperature, isAutoEnabled ->
                TemperatureUiState(
                    currentTemperatureKelvin = temperature,
                    isAutoTemperatureEnabled = isAutoEnabled,
                    isLoading = false,
                    errorMessage = null // Clear error on success
                )
            }
                .catch { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Failed to get temperature settings: ${throwable.message}"
                        )
                    }
                }
                .collect { combinedState ->
                    _uiState.value = combinedState // Update the whole state at once
                }
        }
    }

    fun onTemperatureChange(newKelvin: Int) {
        // Update UI immediately
        _uiState.update { it.copy(currentTemperatureKelvin = newKelvin) }

        viewModelScope.launch {
            val result = setColorTemperatureUseCase(newKelvin)
            if (result.isFailure) {
                _uiState.update {
                    it.copy(errorMessage = "Failed to set temperature: ${result.exceptionOrNull()?.message}")
                }
                // Optionally revert UI or re-fetch
                // fetchInitialTemperatureAndAutoState()
            } else {
                // Clear error on success
                _uiState.update { it.copy(errorMessage = null) }
            }
        }
    }

    fun onAutoTemperatureToggle(enabled: Boolean) {
         _uiState.update { it.copy(isAutoTemperatureEnabled = enabled) }
         viewModelScope.launch {
            val result = colorTemperatureRepository.setAutoTemperatureEnabled(enabled)
             if (result.isFailure) {
                 _uiState.update {
                     it.copy(
                         isAutoTemperatureEnabled = !enabled, // Revert toggle on failure
                         errorMessage = "Failed to toggle auto-temperature: ${result.exceptionOrNull()?.message}"
                     )
                 }
             } else {
                  // Clear error on success
                 _uiState.update { it.copy(errorMessage = null) }
             }
         }
    }
}