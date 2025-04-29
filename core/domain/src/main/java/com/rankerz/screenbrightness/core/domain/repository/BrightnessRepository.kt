package com.rankerz.screenbrightness.core.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing screen brightness settings.
 */
interface BrightnessRepository {

    /**
     * Gets the current application-controlled brightness level as a flow.
     * This might differ from the system brightness if the app overlay is active.
     */
    fun getCurrentBrightness(): Flow<Float>

    /**
     * Sets the application-controlled brightness level.
     * This typically involves controlling an overlay or directly setting system brightness
     * if permissions allow and settings permit.
     *
     * @param level The desired brightness level (e.g., 0.0f to 1.0f).
     * @return Result indicating success or failure.
     */
    suspend fun setBrightness(level: Float): Result<Unit>

    /**
     * Gets the current system brightness level as a flow.
     */
    fun getSystemBrightness(): Flow<Float>

    /**
     * Sets the system brightness level directly.
     * Requires appropriate system permissions.
     *
     * @param level The desired brightness level (e.g., 0.0f to 1.0f).
     * @return Result indicating success or failure (e.g., permission denied).
     */
    suspend fun setSystemBrightness(level: Float): Result<Unit>

    /**
     * Checks if adaptive brightness (auto-brightness) is currently enabled in the system settings.
     */
    fun isAdaptiveBrightnessEnabled(): Flow<Boolean>

    /**
     * Enables or disables adaptive brightness (auto-brightness) in the system settings.
     * Requires appropriate system permissions.
     *
     * @param enabled True to enable adaptive brightness, false to disable.
     * @return Result indicating success or failure (e.g., permission denied).
     */
    suspend fun setAdaptiveBrightnessEnabled(enabled: Boolean): Result<Unit>
}