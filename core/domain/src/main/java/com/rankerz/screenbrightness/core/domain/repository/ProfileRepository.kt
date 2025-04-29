package com.rankerz.screenbrightness.core.domain.repository

import com.rankerz.screenbrightness.core.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the contract for managing user profiles.
 */
interface ProfileRepository {

    /**
     * Retrieves a flow of all saved user profiles.
     */
    fun getAllProfiles(): Flow<List<UserProfile>>

    /**
     * Retrieves a specific user profile by its ID.
     * Implementations should return null if the profile is not found.
     */
    suspend fun getProfileById(profileId: String): UserProfile? // Optional: Add if needed

    /**
     * Adds a new user profile.
     *
     * @param profile The UserProfile object to add.
     * @return Result indicating success or failure.
     */
    suspend fun addProfile(profile: UserProfile): Result<Unit>

    /**
     * Updates an existing user profile.
     *
     * @param profile The UserProfile object with updated details. The ID should match an existing profile.
     * @return Result indicating success or failure.
     */
    suspend fun updateProfile(profile: UserProfile): Result<Unit>

    /**
     * Deletes a user profile by its ID.
     *
     * @param profileId The unique ID of the profile to delete.
     * @return Result indicating success or failure.
     */
    suspend fun deleteProfile(profileId: String): Result<Unit>

    // Optional: Consider if applying a profile should be a repository concern
    // or handled purely by use cases combining SetBrightness/SetTemperature.
    // If handled here, the implementation would likely call the respective repositories.
    // suspend fun applyProfileSettings(profileId: String): Result<Unit>
}