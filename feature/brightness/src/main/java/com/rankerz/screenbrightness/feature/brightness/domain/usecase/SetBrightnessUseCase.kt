package com.rankerz.screenbrightness.feature.brightness.domain.usecase

import com.rankerz.screenbrightness.core.domain.repository.BrightnessRepository
import javax.inject.Inject

/**
 * Use case to set the brightness level.
 */
class SetBrightnessUseCase @Inject constructor(
    private val brightnessRepository: BrightnessRepository
) {
    /**
     * Executes the use case to set the brightness level.
     *
     * @param level The desired brightness level (Float).
     * @return A Result indicating success or failure (Result<Unit>).
     */
    suspend operator fun invoke(level: Float): Result<Unit> {
        return brightnessRepository.setBrightness(level)
    }
}