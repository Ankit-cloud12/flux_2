package com.rankerz.screenbrightness.feature.perapp.domain.usecase

import com.rankerz.screenbrightness.core.domain.repository.AppProfileRepository
import javax.inject.Inject

/**
 * Use case to enable or disable the per-app brightness feature.
 */
class SetPerAppBrightnessEnabledUseCase @Inject constructor(
    private val appProfileRepository: AppProfileRepository
) {
    suspend operator fun invoke(enabled: Boolean): Result<Unit> {
        return appProfileRepository.setPerAppBrightnessEnabled(enabled)
    }
}