package com.rankerz.screenbrightness.core.domain.model

// Placeholder data class for ColorProfile
data class ColorProfile(
    val id: Long,
    val name: String,
    val red: Float,
    val green: Float,
    val blue: Float,
    val colorTemperature: Int? // Optional temperature if profile is based on it
)