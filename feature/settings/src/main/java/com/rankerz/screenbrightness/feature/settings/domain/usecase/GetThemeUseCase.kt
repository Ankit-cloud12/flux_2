package com.rankerz.screenbrightness.feature.settings.domain.usecase

import com.rankerz.screenbrightness.core.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get the currently selected app theme preference.
 */
class GetThemeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<String> {
        return settingsRepository.getAppTheme()
    }
}