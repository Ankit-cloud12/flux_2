package com.rankerz.screenbrightness.feature.profiles.domain.usecase

import com.rankerz.screenbrightness.core.domain.model.UserProfile
import com.rankerz.screenbrightness.core.domain.repository.ProfileRepository
import javax.inject.Inject

/**
 * Use case to update an existing user profile.
 */
class UpdateProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository // Assuming this interface exists
) {
    suspend operator fun invoke(profile: UserProfile): Result<Unit> {
        // Add validation if needed
        // println("Stub: Updating profile: $profile") // Placeholder removed
        return profileRepository.updateProfile(profile)
    }
}