package com.rankerz.screenbrightness.core.domain.repository

import com.rankerz.screenbrightness.core.domain.model.AppProfile
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing per-application brightness/color profiles.
 */
interface AppProfileRepository {

    /**
     * Gets all saved per-application profiles as a flow.
     */
    fun getAppProfiles(): Flow<List<AppProfile>>

    /**
     * Saves a new per-application profile or updates an existing one based on the package name.
     *
     * @param profile The AppProfile object to save.
     * @return Result indicating success or failure.
     */
    suspend fun saveAppProfile(profile: AppProfile): Result<Unit>

    /**
     * Deletes a specific per-application profile.
     *
     * @param packageName The package name of the application profile to delete.
     * @return Result indicating success or failure.
     */
    suspend fun deleteAppProfile(packageName: String): Result<Unit>

    /**
     * Gets the specific profile for a given application package name as a flow.
     * Returns null if no profile exists for the package name.
     *
     * @param packageName The package name of the application.
     */
    fun getProfileForApp(packageName: String): Flow<AppProfile?>
}