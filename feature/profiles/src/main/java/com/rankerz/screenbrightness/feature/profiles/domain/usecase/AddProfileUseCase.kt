package com.rankerz.screenbrightness.feature.profiles.domain.usecase

import com.rankerz.screenbrightness.core.domain.model.UserProfile
import com.rankerz.screenbrightness.core.domain.repository.ProfileRepository
import javax.inject.Inject

/**
 * Use case to add a new user profile.
 */
class AddProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository // Assuming this interface exists
) {
    suspend operator fun invoke(profile: UserProfile): Result<Unit> {
        // Add validation if needed (e.g., check name uniqueness, value ranges)
        // println("Stub: Adding profile: $profile") // Placeholder removed
        return profileRepository.addProfile(profile)
    }
}