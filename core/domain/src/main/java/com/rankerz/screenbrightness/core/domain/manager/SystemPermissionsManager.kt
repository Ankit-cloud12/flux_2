package com.rankerz.screenbrightness.core.domain.manager

import kotlinx.coroutines.flow.Flow

/**
 * Interface for managing system-level permissions required by the application.
 * Implementations will interact with the Android system to check and request permissions.
 */
interface SystemPermissionsManager {

    /**
     * Checks if the permission to modify system settings (like brightness) is granted.
     * Corresponds to `android.permission.WRITE_SETTINGS`.
     */
    fun hasWriteSettingsPermission(): Flow<Boolean>

    /**
     * Requests the permission to modify system settings.
     * The implementation will likely navigate the user to the system settings screen
     * where they can manually grant the permission.
     *
     * @return Result indicating if the navigation attempt was made (doesn't guarantee permission granted).
     */
    suspend fun requestWriteSettingsPermission(): Result<Unit> // Result might just indicate navigation attempt

    /**
     * Checks if the permission to draw over other apps (for the overlay) is granted.
     * Corresponds to `android.permission.SYSTEM_ALERT_WINDOW`.
     */
    fun hasOverlayPermission(): Flow<Boolean>

    /**
     * Requests the permission to draw over other apps.
     * The implementation will likely navigate the user to the system settings screen
     * where they can manually grant the permission.
     *
     * @return Result indicating if the navigation attempt was made (doesn't guarantee permission granted).
     */
    suspend fun requestOverlayPermission(): Result<Unit> // Result might just indicate navigation attempt

    /**
     * Checks if the location permission (needed for sunrise/sunset) is granted.
     * Should check for at least coarse location (`ACCESS_COARSE_LOCATION`) or fine location (`ACCESS_FINE_LOCATION`).
     */
    fun hasLocationPermission(): Flow<Boolean>

    /**
     * Requests location permission(s).
     * The implementation will handle the standard Android permission request flow.
     * Consider requesting coarse or fine based on feature requirements.
     *
     * @return Result indicating if the permission was granted after the request flow.
     */
    suspend fun requestLocationPermission(): Result<Boolean> // True if granted, False otherwise

     /**
      * Checks if the permission to access usage stats (for per-app profiles) is granted.
      * This requires navigating the user to a specific system settings screen.
      */
     fun hasUsageStatsPermission(): Flow<Boolean>

     /**
      * Requests the permission to access usage stats.
      * The implementation will likely navigate the user to the relevant system settings screen.
      *
      * @return Result indicating if the navigation attempt was made (doesn't guarantee permission granted).
      */
     suspend fun requestUsageStatsPermission(): Result<Unit> // Result might just indicate navigation attempt

    // TODO: Add checks/requests for other necessary permissions as features are implemented
    // e.g., Notifications (POST_NOTIFICATIONS on API 33+), Background execution permissions, etc.
}