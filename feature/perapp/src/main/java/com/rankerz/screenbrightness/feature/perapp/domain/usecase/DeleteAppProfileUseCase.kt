package com.rankerz.screenbrightness.feature.perapp.domain.usecase

import com.rankerz.screenbrightness.core.domain.repository.AppProfileRepository
import javax.inject.Inject

/**
 * Use case to delete a per-app brightness profile by package name.
 */
class DeleteAppProfileUseCase @Inject constructor(
    private val appProfileRepository: AppProfileRepository
) {
    suspend operator fun invoke(packageName: String): Result<Unit> {
        return appProfileRepository.deleteAppProfile(packageName)
    }
}