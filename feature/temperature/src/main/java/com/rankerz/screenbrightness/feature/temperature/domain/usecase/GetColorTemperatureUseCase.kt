package com.rankerz.screenbrightness.feature.temperature.domain.usecase

import com.rankerz.screenbrightness.core.domain.repository.ColorTemperatureRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get the current color temperature setting.
 */
class GetColorTemperatureUseCase @Inject constructor(
    private val colorTemperatureRepository: ColorTemperatureRepository
) {
    operator fun invoke(): Flow<Int> {
        return colorTemperatureRepository.getColorTemperatureKelvin()
    }
}