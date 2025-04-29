package com.rankerz.screenbrightness.feature.settings.ui

/**
 * Represents the state for the Settings screen.
 *
 * @param hasOverlayPermission Indicates if the overlay permission is granted.
 * @param hasWriteSettingsPermission Indicates if the write settings permission is granted.
 * @param isLoading Indicates if the initial permission checks are in progress.
 * @param errorMessage An optional error message to display.
 * @param startOnBootEnabled Whether the app should start its service on device boot.
 * @param selectedTheme The currently selected app theme ("System", "Light", "Dark").
 */
data class SettingsUiState(
    val hasOverlayPermission: Boolean = false,
    val hasWriteSettingsPermission: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val startOnBootEnabled: Boolean = false, // Default value
    val selectedTheme: String = "System" // Default value
)