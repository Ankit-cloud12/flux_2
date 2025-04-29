package com.rankerz.screenbrightness.core.domain.model

// Placeholder data class for AppProfile
data class AppProfile(
    val packageName: String,
    val brightness: Float?, // Nullable if using default
    val colorTemperature: Int?, // Nullable if using default
    val isEnabled: Boolean
)