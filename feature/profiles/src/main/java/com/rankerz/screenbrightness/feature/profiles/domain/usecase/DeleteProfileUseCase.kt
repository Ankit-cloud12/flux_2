package com.rankerz.screenbrightness.feature.profiles.domain.usecase

import com.rankerz.screenbrightness.core.domain.repository.ProfileRepository
import javax.inject.Inject

/**
 * Use case to delete a user profile by its ID.
 */
class DeleteProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository // Assuming this interface exists
) {
    suspend operator fun invoke(profileId: String): Result<Unit> {
        // println("Stub: Deleting profile with ID: $profileId") // Placeholder removed
        return profileRepository.deleteProfile(profileId)
    }
}