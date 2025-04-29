package com.rankerz.screenbrightness.feature.brightness.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rankerz.screenbrightness.feature.brightness.domain.usecase.GetBrightnessUseCase
import com.rankerz.screenbrightness.feature.brightness.domain.usecase.GetMinimumAllowedBrightnessUseCase
import com.rankerz.screenbrightness.feature.brightness.domain.usecase.IsOverlayRequiredUseCase
import com.rankerz.screenbrightness.feature.brightness.domain.usecase.SetBrightnessUseCase
import com.rankerz.screenbrightness.feature.brightness.ui.BrightnessUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrightnessViewModel @Inject constructor(
    private val getBrightnessUseCase: GetBrightnessUseCase,
    private val setBrightnessUseCase: SetBrightnessUseCase,
    private val isOverlayRequiredUseCase: IsOverlayRequiredUseCase,
    private val getMinimumAllowedBrightnessUseCase: GetMinimumAllowedBrightnessUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BrightnessUiState())
    val uiState: StateFlow<BrightnessUiState> = _uiState.asStateFlow()

    init {
        fetchInitialBrightness()
        fetchMinimumBrightness()
    }

    private fun fetchInitialBrightness() {
        viewModelScope.launch {
            getBrightnessUseCase()
                .catch { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Failed to get brightness: ${throwable.message}"
                        )
                    }
                }
                .collect { brightness ->
                    _uiState.update {
                        it.copy(
                            currentBrightness = brightness,
                            isLoading = false,
                            errorMessage = null // Clear error on success
                        )
                    }
                    checkOverlayRequirement(brightness) // Check overlay after getting initial value
                }
        }
    }

    private fun fetchMinimumBrightness() {
        viewModelScope.launch {
            try {
                val minBrightness = getMinimumAllowedBrightnessUseCase()
                _uiState.update { it.copy(minBrightness = minBrightness) }
            } catch (e: Exception) {
                // Handle error fetching min brightness if necessary, maybe log it
                 _uiState.update {
                    it.copy(errorMessage = "Failed to get minimum brightness: ${e.message}")
                 }
            }
        }
    }


    fun onBrightnessChange(newLevel: Float) {
        // Update UI immediately for responsiveness
        _uiState.update { it.copy(currentBrightness = newLevel) }

        viewModelScope.launch {
            // Check overlay requirement before setting
            checkOverlayRequirement(newLevel)

            // Set the brightness via the use case
            val result = setBrightnessUseCase(newLevel)
            if (result.isFailure) {
                _uiState.update {
                    it.copy(errorMessage = "Failed to set brightness: ${result.exceptionOrNull()?.message}")
                }
                // Optionally revert UI state if setting fails, though slider might jump
                // fetchInitialBrightness() // Re-fetch to get actual current value
            } else {
                 // Clear error on success
                 _uiState.update { it.copy(errorMessage = null) }
            }
        }
    }

    private suspend fun checkOverlayRequirement(level: Float) {
         try {
            val required = isOverlayRequiredUseCase(level)
            _uiState.update { it.copy(isOverlayRequired = required) }
        } catch (e: Exception) {
             _uiState.update {
                it.copy(errorMessage = "Failed to check overlay requirement: ${e.message}")
             }
        }
    }
}