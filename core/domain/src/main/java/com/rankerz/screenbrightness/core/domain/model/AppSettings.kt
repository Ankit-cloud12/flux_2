package com.rankerz.screenbrightness.core.domain.model

// Placeholder data class for AppSettings
data class AppSettings(
    val useSystemBrightness: Boolean,
    val defaultBrightness: Float,
    val defaultColorTemperature: Int,
    val locationBasedSchedulingEnabled: Boolean,
    val sunriseTime: Long?, // Consider using a more appropriate time type
    val sunsetTime: Long?,  // Consider using a more appropriate time type
    val notificationEnabled: Boolean,
    val overlayEnabled: Boolean
    // Add other settings as needed
)