package com.rankerz.screenbrightness.feature.brightness.domain.usecase

import com.rankerz.screenbrightness.core.domain.repository.BrightnessRepository
import javax.inject.Inject

/**
 * Use case to determine if an overlay is required for a given brightness level.
 */
class IsOverlayRequiredUseCase @Inject constructor(
    private val brightnessRepository: BrightnessRepository
) {
    /**
     * Executes the use case to check if an overlay is required.
     *
     * @param level The brightness level to check (Float).
     * @return True if an overlay is required, false otherwise (Boolean).
     */
    operator fun invoke(level: Float): Boolean {
        return brightnessRepository.isOverlayRequired(level)
    }
}