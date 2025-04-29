package com.rankerz.screenbrightness.feature.brightness.domain.usecase

import com.rankerz.screenbrightness.core.domain.repository.BrightnessRepository
import javax.inject.Inject

/**
 * Use case to get the minimum allowed brightness level.
 */
class GetMinimumAllowedBrightnessUseCase @Inject constructor(
    private val brightnessRepository: BrightnessRepository
) {
    /**
     * Executes the use case to get the minimum allowed brightness level.
     *
     * @return The minimum allowed brightness level (Float).
     */
    operator fun invoke(): Float {
        return brightnessRepository.getMinimumAllowedBrightness()
    }
}