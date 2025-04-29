package com.rankerz.screenbrightness.core.data.repository

import com.rankerz.screenbrightness.core.domain.repository.ColorTemperatureRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

// TODO: Inject actual LocalDataSource and SystemDataSource
class ColorTemperatureRepositoryImpl @Inject constructor() : ColorTemperatureRepository {

    override fun getColorTemperatureKelvin(): Flow<Int> {
        // Stub implementation
        return flowOf(6500) // Default temperature (neutral white)
    }

    override suspend fun setColorTemperatureKelvin(kelvin: Int): Result<Unit> {
        // Stub implementation
        println("Stub: Setting color temperature to $kelvin K")
        return Result.success(Unit)
    }

    override fun getAutoTemperatureEnabled(): Flow<Boolean> {
        // Stub implementation
        return flowOf(false)
    }

     override suspend fun setAutoTemperatureEnabled(enabled: Boolean): Result<Unit> {
        // Stub implementation
        println("Stub: Setting auto-temperature enabled to $enabled")
        return Result.success(Unit)
    }
}