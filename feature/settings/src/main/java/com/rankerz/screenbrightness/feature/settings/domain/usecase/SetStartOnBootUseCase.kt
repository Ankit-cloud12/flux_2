package com.rankerz.screenbrightness.feature.settings.domain.usecase

import com.rankerz.screenbrightness.core.domain.repository.SettingsRepository
import javax.inject.Inject

/**
 * Use case to set the "Start on Boot" preference.
 */
class SetStartOnBootUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(enabled: Boolean): Result<Unit> {
        return settingsRepository.setStartOnBootEnabled(enabled)
    }
}