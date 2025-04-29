package com.rankerz.screenbrightness.core.domain.repository

import com.rankerz.screenbrightness.core.domain.model.ColorProfile
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing screen color temperature and profiles.
 */
interface ColorTemperatureRepository {

    /**
     * Gets the current application-controlled color temperature as a flow.
     */
    fun getCurrentColorTemperature(): Flow<Int>

    /**
     * Sets the application-controlled color temperature.
     * This typically involves controlling an overlay.
     *
     * @param temperature The desired color temperature (e.g., in Kelvin).
     * @return Result indicating success or failure.
     */
    suspend fun setColorTemperature(temperature: Int): Result<Unit>

    /**
     * Gets a list of saved color profiles as a flow.
     */
    fun getColorProfiles(): Flow<List<ColorProfile>>

    /**
     * Saves a new color profile or updates an existing one.
     *
     * @param profile The ColorProfile object to save.
     * @return Result indicating success or failure.
     */
    suspend fun saveColorProfile(profile: ColorProfile): Result<Unit>

    /**
     * Deletes a specific color profile.
     *
     * @param profileId The ID of the color profile to delete.
     * @return Result indicating success or failure.
     */
    suspend fun deleteColorProfile(profileId: Long): Result<Unit>

    /**
     * Applies a specific color profile, setting the color temperature based on the profile.
     *
     * @param profileId The ID of the color profile to apply.
     * @return Result indicating success or failure.
     */
    suspend fun applyColorProfile(profileId: Long): Result<Unit>
}