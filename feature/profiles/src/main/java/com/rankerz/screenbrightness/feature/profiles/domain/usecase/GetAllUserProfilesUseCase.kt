package com.rankerz.screenbrightness.feature.profiles.domain.usecase

import com.rankerz.screenbrightness.core.domain.model.UserProfile
import com.rankerz.screenbrightness.core.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


/**
 * Use case to get a list of all saved user profiles.
 */
class GetAllUserProfilesUseCase @Inject constructor(
    private val profileRepository: ProfileRepository // Assuming this interface exists
) {
    operator fun invoke(): Flow<List<UserProfile>> {
        return profileRepository.getAllProfiles()
    }
}