package com.rankerz.screenbrightness.core.domain.model

import java.util.UUID

/**
 * Represents a user-defined profile combining brightness and color temperature settings.
 *
 * @param id Unique identifier for the profile.
 * @param name User-defined name for the profile.
 * @param brightnessLevel The brightness level (e.g., 0.0f to 1.0f).
 * @param colorTemperatureKelvin The color temperature in Kelvin (e.g., 1000 to 10000).
 */
data class UserProfile(
    val id: String = UUID.randomUUID().toString(), // Auto-generate ID by default
    val name: String,
    val brightnessLevel: Float,
    val colorTemperatureKelvin: Int
)