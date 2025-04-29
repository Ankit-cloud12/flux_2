package com.rankerz.screenbrightness.core.domain.repository

import com.rankerz.screenbrightness.core.domain.model.LocationSettings
import com.rankerz.screenbrightness.core.domain.model.Schedule
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing time-based and location-based scheduling.
 */
interface SchedulingRepository {

    /**
     * Gets all saved schedules as a flow.
     */
    fun getSchedules(): Flow<List<Schedule>>

    /**
     * Adds a new schedule.
     *
     * @param schedule The Schedule object to add.
     * @return Result indicating success or failure.
     */
    suspend fun addSchedule(schedule: Schedule): Result<Unit>

    /**
     * Updates an existing schedule.
     *
     * @param schedule The Schedule object with updated details.
     * @return Result indicating success or failure.
     */
    suspend fun updateSchedule(schedule: Schedule): Result<Unit>

    /**
     * Deletes a specific schedule.
     *
     * @param scheduleId The ID of the schedule to delete.
     * @return Result indicating success or failure.
     */
    suspend fun deleteSchedule(scheduleId: Long): Result<Unit>

    /**
     * Gets the current location settings used for scheduling (e.g., sunrise/sunset).
     */
    fun getLocationSettings(): Flow<LocationSettings>

    /**
     * Updates the location settings.
     *
     * @param settings The updated LocationSettings object.
     * @return Result indicating success or failure.
     */
    suspend fun updateLocationSettings(settings: LocationSettings): Result<Unit>

    /**
     * Gets the calculated sunrise and sunset times based on location settings as a flow.
     * Returns a Pair where first is sunrise time (e.g., timestamp), second is sunset time.
     * Returns null Pair if location data is unavailable or calculation fails.
     */
    fun getSunriseSunsetTimes(): Flow<Pair<Long, Long>?> // Consider a dedicated data class later
}