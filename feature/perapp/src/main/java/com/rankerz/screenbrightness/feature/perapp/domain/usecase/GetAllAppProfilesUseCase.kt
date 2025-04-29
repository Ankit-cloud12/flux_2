package com.rankerz.screenbrightness.feature.perapp.domain.usecase

import com.rankerz.screenbrightness.core.domain.model.AppProfile
import com.rankerz.screenbrightness.core.domain.repository.AppProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get a list of all saved per-app brightness profiles.
 */
class GetAllAppProfilesUseCase @Inject constructor(
    private val appProfileRepository: AppProfileRepository
) {
    operator fun invoke(): Flow<List<AppProfile>> {
        return appProfileRepository.getAllAppProfiles()
    }
}