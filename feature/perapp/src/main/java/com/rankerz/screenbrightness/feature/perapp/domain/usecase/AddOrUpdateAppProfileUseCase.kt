package com.rankerz.screenbrightness.feature.perapp.domain.usecase

import com.rankerz.screenbrightness.core.domain.model.AppProfile
import com.rankerz.screenbrightness.core.domain.repository.AppProfileRepository
import javax.inject.Inject

/**
 * Use case to add a new per-app brightness profile or update an existing one.
 */
class AddOrUpdateAppProfileUseCase @Inject constructor(
    private val appProfileRepository: AppProfileRepository
) {
    suspend operator fun invoke(appProfile: AppProfile): Result<Unit> {
        // Add validation if needed (e.g., check brightness range)
        return appProfileRepository.addOrUpdateAppProfile(appProfile)
    }
}