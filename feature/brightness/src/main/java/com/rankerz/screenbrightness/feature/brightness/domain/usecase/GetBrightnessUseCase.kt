package com.rankerz.screenbrightness.feature.brightness.domain.usecase

import com.rankerz.screenbrightness.core.domain.repository.BrightnessRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get the current brightness level.
 */
class GetBrightnessUseCase @Inject constructor(
    private val brightnessRepository: BrightnessRepository
) {
    /**
     * Executes the use case to get the current brightness level.
     *
     * @return A Flow emitting the current brightness level (Float).
     */
    operator fun invoke(): Flow<Float> {
        return brightnessRepository.getCurrentBrightness()
    }
}