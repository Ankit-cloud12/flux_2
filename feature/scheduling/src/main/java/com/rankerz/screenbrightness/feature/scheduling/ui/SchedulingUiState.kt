package com.rankerz.screenbrightness.feature.scheduling.ui

import com.rankerz.screenbrightness.core.domain.model.Schedule

/**
 * Represents the state for the Scheduling screen.
 *
 * @param schedules The list of currently configured schedules.
 * @param isLoading Whether the screen is currently loading data.
 * @param errorMessage An optional error message to display.
 * // Add other states as needed, e.g., location settings, currently active schedule.
 */
data class SchedulingUiState(
    val schedules: List<Schedule> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
    // val locationSettings: LocationSettings? = null // Example for location-based scheduling
)