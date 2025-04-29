package com.rankerz.screenbrightness.feature.perapp.domain.usecase

import com.rankerz.screenbrightness.core.domain.model.AppProfile
import com.rankerz.screenbrightness.core.domain.repository.AppProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get a specific per-app brightness profile by package name.
 */
class GetAppProfileUseCase @Inject constructor(
    private val appProfileRepository: AppProfileRepository
) {
    suspend operator fun invoke(packageName: String): Flow<AppProfile?> {
        return appProfileRepository.getAppProfile(packageName)
    }
}