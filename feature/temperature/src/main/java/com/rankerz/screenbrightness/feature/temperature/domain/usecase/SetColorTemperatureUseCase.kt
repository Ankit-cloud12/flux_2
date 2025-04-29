package com.rankerz.screenbrightness.feature.temperature.domain.usecase

import com.rankerz.screenbrightness.core.domain.repository.ColorTemperatureRepository
import javax.inject.Inject

/**
 * Use case to set the color temperature.
 */
class SetColorTemperatureUseCase @Inject constructor(
    private val colorTemperatureRepository: ColorTemperatureRepository
) {
    suspend operator fun invoke(kelvin: Int): Result<Unit> {
        // Add validation if needed (e.g., check kelvin range)
        return colorTemperatureRepository.setColorTemperatureKelvin(kelvin)
    }
}