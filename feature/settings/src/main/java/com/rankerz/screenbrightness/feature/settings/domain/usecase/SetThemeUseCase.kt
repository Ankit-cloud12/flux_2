package com.rankerz.screenbrightness.feature.settings.domain.usecase

import com.rankerz.screenbrightness.core.domain.repository.SettingsRepository
import javax.inject.Inject

/**
 * Use case to set the app theme preference.
 */
class SetThemeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(theme: String): Result<Unit> {
        // Validation is handled in the repository implementation
        return settingsRepository.setAppTheme(theme)
    }
}