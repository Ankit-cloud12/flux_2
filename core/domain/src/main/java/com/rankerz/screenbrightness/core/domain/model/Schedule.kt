package com.rankerz.screenbrightness.core.domain.model

// Placeholder data class for Schedule
data class Schedule(
    val id: Long,
    val startTime: Long, // Consider using a more appropriate time type later
    val endTime: Long,   // Consider using a more appropriate time type later
    val brightness: Float?,
    val colorTemperature: Int?,
    val daysOfWeek: List<Int>, // e.g., 1 for Monday, 7 for Sunday
    val isEnabled: Boolean
)