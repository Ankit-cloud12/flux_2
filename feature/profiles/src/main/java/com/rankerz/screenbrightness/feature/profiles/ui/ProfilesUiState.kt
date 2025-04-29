package com.rankerz.screenbrightness.feature.profiles.ui

import com.rankerz.screenbrightness.core.domain.model.UserProfile

/**
 * Represents the state for the Profiles screen.
 *
 * @param profiles The list of available user profiles.
 * @param isLoading Whether the screen is currently loading data.
 * @param errorMessage An optional error message to display.
 * @param selectedProfileId The ID of the currently active or selected profile, if any.
 */
data class ProfilesUiState(
    val profiles: List<UserProfile> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val selectedProfileId: String? = null // To highlight the active profile
)