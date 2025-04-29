package com.rankerz.screenbrightness.feature.temperature.ui

/**
 * Represents the state for the Temperature screen.
 *
 * @param currentTemperatureKelvin The current color temperature value in Kelvin.
 * @param isLoading Whether the screen is currently loading data.
 * @param errorMessage An optional error message to display.
 * @param isAutoTemperatureEnabled Whether automatic temperature adjustment is enabled.
 */
data class TemperatureUiState(
    val currentTemperatureKelvin: Int = 6500, // Default neutral white
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val isAutoTemperatureEnabled: Boolean = false
)