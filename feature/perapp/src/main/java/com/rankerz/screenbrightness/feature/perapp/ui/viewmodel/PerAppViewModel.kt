package com.rankerz.screenbrightness.feature.perapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rankerz.screenbrightness.core.domain.model.AppProfile
import com.rankerz.screenbrightness.feature.perapp.domain.usecase.AddOrUpdateAppProfileUseCase
import com.rankerz.screenbrightness.feature.perapp.domain.usecase.DeleteAppProfileUseCase
import com.rankerz.screenbrightness.feature.perapp.domain.usecase.GetAllAppProfilesUseCase
import com.rankerz.screenbrightness.feature.perapp.domain.usecase.GetPerAppBrightnessEnabledUseCase
import com.rankerz.screenbrightness.feature.perapp.domain.usecase.SetPerAppBrightnessEnabledUseCase
import com.rankerz.screenbrightness.feature.perapp.ui.PerAppUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerAppViewModel @Inject constructor(
    private val getAllAppProfilesUseCase: GetAllAppProfilesUseCase,
    private val addOrUpdateAppProfileUseCase: AddOrUpdateAppProfileUseCase,
    private val deleteAppProfileUseCase: DeleteAppProfileUseCase,
    private val getPerAppBrightnessEnabledUseCase: GetPerAppBrightnessEnabledUseCase,
    private val setPerAppBrightnessEnabledUseCase: SetPerAppBrightnessEnabledUseCase
    // TODO: Inject use case to get installed apps later
) : ViewModel() {

    private val _uiState = MutableStateFlow(PerAppUiState())
    val uiState: StateFlow<PerAppUiState> = _uiState.asStateFlow()

    init {
        fetchInitialState()
    }

    private fun fetchInitialState() {
        viewModelScope.launch {
            combine(
                getAllAppProfilesUseCase(),
                getPerAppBrightnessEnabledUseCase()
            ) { profiles, isEnabled ->
                PerAppUiState(
                    appProfiles = profiles,
                    isEnabled = isEnabled,
                    isLoading = false,
                    errorMessage = null
                )
            }
                .catch { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Failed to load per-app settings: ${throwable.message}"
                        )
                    }
                }
                .collect { combinedState ->
                    _uiState.value = combinedState
                }
        }
    }

    fun onPerAppToggle(enabled: Boolean) {
        _uiState.update { it.copy(isEnabled = enabled) } // Optimistic update
        viewModelScope.launch {
            val result = setPerAppBrightnessEnabledUseCase(enabled)
            if (result.isFailure) {
                _uiState.update {
                    it.copy(
                        isEnabled = !enabled, // Revert on failure
                        errorMessage = "Failed to toggle per-app feature: ${result.exceptionOrNull()?.message}"
                    )
                }
            } else {
                 _uiState.update { it.copy(errorMessage = null) } // Clear error
            }
        }
    }

    fun onAddOrUpdateProfile(appProfile: AppProfile) {
        viewModelScope.launch {
            val result = addOrUpdateAppProfileUseCase(appProfile)
            if (result.isFailure) {
                _uiState.update {
                    it.copy(errorMessage = "Failed to save profile for ${appProfile.packageName}: ${result.exceptionOrNull()?.message}")
                }
            } else {
                 _uiState.update { it.copy(errorMessage = null) } // Clear error
                 // Re-fetch might be needed if not observing the flow directly
                 // fetchInitialState()
            }
        }
    }

    fun onDeleteProfile(packageName: String) {
        viewModelScope.launch {
            val result = deleteAppProfileUseCase(packageName)
            if (result.isFailure) {
                _uiState.update {
                    it.copy(errorMessage = "Failed to delete profile for $packageName: ${result.exceptionOrNull()?.message}")
                }
            } else {
                 _uiState.update { it.copy(errorMessage = null) } // Clear error
                 // Re-fetch might be needed if not observing the flow directly
                 // fetchInitialState()
            }
        }
    }

    // TODO: Add function to load installed apps
}