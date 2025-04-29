package com.rankerz.screenbrightness.core.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the contract for managing app settings preferences.
 */
interface SettingsRepository {

    /**
     * Retrieves a flow indicating if the "Start on Boot" setting is enabled.
     */
    fun getStartOnBootEnabled(): Flow<Boolean>

    /**
     * Sets the "Start on Boot" preference.
     */
    suspend fun setStartOnBootEnabled(enabled: Boolean): Result<Unit>

    /**
     * Retrieves a flow of the currently selected app theme ("System", "Light", "Dark").
     */
    fun getAppTheme(): Flow<String>

    /**
     * Sets the app theme preference.
     */
    suspend fun setAppTheme(theme: String): Result<Unit>

    // Add other settings methods here as needed
}