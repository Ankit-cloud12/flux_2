package com.rankerz.screenbrightness.feature.profiles.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rankerz.screenbrightness.feature.profiles.domain.usecase.AddProfileUseCase
import com.rankerz.screenbrightness.feature.profiles.domain.usecase.ApplyProfileUseCase
import com.rankerz.screenbrightness.feature.profiles.domain.usecase.DeleteProfileUseCase
import com.rankerz.screenbrightness.feature.profiles.domain.usecase.GetAllUserProfilesUseCase
import com.rankerz.screenbrightness.feature.profiles.domain.usecase.UpdateProfileUseCase
import com.rankerz.screenbrightness.core.domain.model.UserProfile // Correct import
import com.rankerz.screenbrightness.feature.profiles.ui.ProfilesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfilesViewModel @Inject constructor(
    private val getAllUserProfilesUseCase: GetAllUserProfilesUseCase,
    private val addProfileUseCase: AddProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val deleteProfileUseCase: DeleteProfileUseCase,
    private val applyProfileUseCase: ApplyProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfilesUiState())
    val uiState: StateFlow<ProfilesUiState> = _uiState.asStateFlow()

    init {
        fetchProfiles()
    }

    private fun fetchProfiles() {
        viewModelScope.launch {
            getAllUserProfilesUseCase()
                .catch { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Failed to load profiles: ${throwable.message}"
                        )
                    }
                }
                .collect { profiles ->
                    _uiState.update {
                        it.copy(
                            profiles = profiles,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
        }
    }

    fun addProfile(profile: UserProfile) {
        viewModelScope.launch {
            val result = addProfileUseCase(profile)
            if (result.isFailure) {
                _uiState.update {
                    it.copy(errorMessage = "Failed to add profile: ${result.exceptionOrNull()?.message}")
                }
            } else {
                _uiState.update { it.copy(errorMessage = null) }
                // Re-fetch might be needed if not observing the flow directly
                // fetchProfiles()
            }
        }
    }

    fun updateProfile(profile: UserProfile) {
        viewModelScope.launch {
            val result = updateProfileUseCase(profile)
            if (result.isFailure) {
                _uiState.update {
                    it.copy(errorMessage = "Failed to update profile: ${result.exceptionOrNull()?.message}")
                }
            } else {
                 _uiState.update { it.copy(errorMessage = null) }
                 // Re-fetch might be needed if not observing the flow directly
                 // fetchProfiles()
            }
        }
    }

    fun deleteProfile(profileId: String) {
        viewModelScope.launch {
            val result = deleteProfileUseCase(profileId)
            if (result.isFailure) {
                _uiState.update {
                    it.copy(errorMessage = "Failed to delete profile: ${result.exceptionOrNull()?.message}")
                }
            } else {
                 _uiState.update { it.copy(errorMessage = null) }
                 // Re-fetch might be needed if not observing the flow directly
                 // fetchProfiles()
            }
        }
    }

    fun applyProfile(profileId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(selectedProfileId = profileId) } // Optimistic update
            val result = applyProfileUseCase(profileId)
            if (result.isFailure) {
                _uiState.update {
                    it.copy(
                        selectedProfileId = null, // Revert selection on failure
                        errorMessage = "Failed to apply profile: ${result.exceptionOrNull()?.message}"
                    )
                }
            } else {
                 _uiState.update { it.copy(errorMessage = null) }
            }
        }
    }
     // Overload to apply directly from a UserProfile object if needed
     fun applyProfile(profile: UserProfile) {
         applyProfile(profile.id) // Delegate to ID-based application
     }
}