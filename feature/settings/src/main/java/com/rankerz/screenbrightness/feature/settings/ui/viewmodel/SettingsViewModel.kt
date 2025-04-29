package com.rankerz.screenbrightness.feature.settings.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rankerz.screenbrightness.feature.settings.domain.usecase.CheckOverlayPermissionUseCase
import com.rankerz.screenbrightness.feature.settings.domain.usecase.* // Import all use cases
import com.rankerz.screenbrightness.feature.settings.ui.SettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.* // Import all flow operators
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val checkOverlayPermissionUseCase: CheckOverlayPermissionUseCase,
    private val requestOverlayPermissionUseCase: RequestOverlayPermissionUseCase,
    private val checkWriteSettingsPermissionUseCase: CheckWriteSettingsPermissionUseCase,
    private val requestWriteSettingsPermissionUseCase: RequestWriteSettingsPermissionUseCase,
    private val getStartOnBootUseCase: GetStartOnBootUseCase, // Inject new use cases
    private val setStartOnBootUseCase: SetStartOnBootUseCase,
    private val getThemeUseCase: GetThemeUseCase,
    private val setThemeUseCase: SetThemeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings() // Load initial settings
    }

    /**
     * Loads the current settings state, including permissions and preferences,
     * and observes changes to preferences.
     */
    fun loadSettings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) } // Indicate loading start

            // Combine preference flows
            combine(
                getStartOnBootUseCase(),
                getThemeUseCase()
            ) { startOnBoot, theme ->
                // Check permissions synchronously each time preferences update (or make them flows too)
                val hasOverlay = checkOverlayPermissionUseCase()
                val hasWriteSettings = checkWriteSettingsPermissionUseCase()

                // Create the new state based on combined values
                SettingsUiState(
                    hasOverlayPermission = hasOverlay,
                    hasWriteSettingsPermission = hasWriteSettings,
                    startOnBootEnabled = startOnBoot,
                    selectedTheme = theme,
                    isLoading = false, // Mark loading finished
                    errorMessage = null // Clear previous errors on successful load/update
                )
            }.catch { e -> // Catch errors from the flows
                Log.e("SettingsViewModel", "Error loading settings preferences", e)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to load settings: ${e.message}"
                    )
                }
            }.collect { newState -> // Collect the combined state
                _uiState.value = newState
            }
        }
    }

    /**
                }
            }
        }
    }

    /**
     * Initiates the request for overlay permission.
     */
    fun onRequestOverlayPermission() {
        try {
            requestOverlayPermissionUseCase()
            // Note: The actual result is handled when the user returns to the app.
            // Call loadSettings() in the Composable's onResume or similar lifecycle event.
        } catch (e: Exception) {
             _uiState.update {
                it.copy(errorMessage = "Failed to request overlay permission: ${e.message}")
             }
        }
    }

    /**
     * Initiates the request for write settings permission.
     */
    fun onRequestWriteSettingsPermission() {
         try {
            requestWriteSettingsPermissionUseCase()
            // Note: The actual result is handled when the user returns to the app.
            // Call loadSettings() in the Composable's onResume or similar lifecycle event.
         } catch (e: Exception) {
              _uiState.update {
                 it.copy(errorMessage = "Failed to request write settings permission: ${e.message}")
              }
         }
    }

    fun onStartOnBootToggled(enabled: Boolean) {
        // No need for optimistic update if observing the flow from repository
        // _uiState.update { it.copy(startOnBootEnabled = enabled) }
        viewModelScope.launch {
            val result = setStartOnBootUseCase(enabled)
            if (result.isFailure) {
                 _uiState.update {
                     // Keep the current UI state for startOnBootEnabled as the flow will update it
                     it.copy(errorMessage = "Failed to update Start on Boot setting: ${result.exceptionOrNull()?.message}")
                 }
            } else {
                 // Error message is cleared automatically when the flow emits the next successful state in loadSettings
                 // _uiState.update { it.copy(errorMessage = null) }
            }
        }
    }

     fun onThemeSelected(theme: String) {
         // Validation is in repository/use case
         // No need for optimistic update if observing the flow
         // _uiState.update { it.copy(selectedTheme = theme) }
         viewModelScope.launch {
             val result = setThemeUseCase(theme)
             if (result.isFailure) {
                  _uiState.update {
                       // Keep the current UI state for selectedTheme as the flow will update it
                      it.copy(errorMessage = "Failed to update theme setting: ${result.exceptionOrNull()?.message}")
                  }
             } else {
                  // Error message is cleared automatically when the flow emits the next successful state in loadSettings
                  // _uiState.update { it.copy(errorMessage = null) }
             }
         }
     }
}