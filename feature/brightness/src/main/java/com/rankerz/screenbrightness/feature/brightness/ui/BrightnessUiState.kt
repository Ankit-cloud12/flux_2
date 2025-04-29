package com.rankerz.screenbrightness.feature.brightness.ui

/**
 * Represents the state of the Brightness screen.
 */
data class BrightnessUiState(
    val currentBrightness: Float = 0.5f, // Default brightness level (0.0 to 1.0)
    val isLoading: Boolean = true, // Indicates if initial data is loading
    val errorMessage: String? = null, // Holds any error message to display
    val isOverlayRequired: Boolean = false, // Indicates if system overlay permission is needed
    val minBrightness: Float = 0.0f // Minimum allowed brightness level
)