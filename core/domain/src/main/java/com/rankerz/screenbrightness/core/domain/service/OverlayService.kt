package com.rankerz.screenbrightness.core.domain.service

import com.rankerz.screenbrightness.core.domain.model.OverlayStatus
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining operations for managing the screen overlay (filter).
 * Implementations will handle the creation, display, update, and removal
 * of the system overlay window.
 */
interface OverlayService {

    /**
     * Shows the screen overlay with the specified initial settings.
     * Requires overlay permission.
     *
     * @param brightness The initial brightness level for the overlay (0.0f to 1.0f, affecting transparency/dimming).
     * @param colorTemperature The initial color temperature to apply (e.g., in Kelvin).
     * @return Result indicating success or failure (e.g., permission denied, service error).
     */
    suspend fun showOverlay(brightness: Float, colorTemperature: Int): Result<Unit>

    /**
     * Updates the properties of the currently displayed overlay.
     * Allows changing brightness and/or color temperature without hiding and showing again.
     *
     * @param brightness The new brightness level (optional, null to keep current).
     * @param colorTemperature The new color temperature (optional, null to keep current).
     * @return Result indicating success or failure.
     */
    suspend fun updateOverlay(brightness: Float?, colorTemperature: Int?): Result<Unit>

    /**
     * Hides and removes the screen overlay.
     *
     * @return Result indicating success or failure.
     */
    suspend fun hideOverlay(): Result<Unit>

    /**
     * Gets the current status of the overlay (e.g., visibility, current settings) as a flow.
     */
    fun getOverlayStatus(): Flow<OverlayStatus>
}