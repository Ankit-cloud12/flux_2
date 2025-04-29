package com.rankerz.screenbrightness.feature.perapp.domain.usecase

import com.rankerz.screenbrightness.core.domain.repository.AppProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to check if the per-app brightness feature is enabled.
 */
class GetPerAppBrightnessEnabledUseCase @Inject constructor(
    private val appProfileRepository: AppProfileRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return appProfileRepository.getPerAppBrightnessEnabled()
    }
}