package com.rankerz.screenbrightness.core.data.repository

import com.rankerz.screenbrightness.core.domain.repository.BrightnessRepository
import com.rankerz.screenbrightness.core.domain.service.OverlayService
import com.rankerz.screenbrightness.data.system.manager.SystemSettingsManager // Import SystemSettingsManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map // Import map
import javax.inject.Inject
import javax.inject.Singleton

// TODO: Inject LocalDataSource if needed for min brightness etc.
@Singleton
class BrightnessRepositoryImpl @Inject constructor(
    private val systemSettingsManager: SystemSettingsManager,
    private val overlayService: OverlayService
) : BrightnessRepository {

    // Converts system brightness (0-255) to float (0.0-1.0)
    private fun Int.toBrightnessFloat(): Float = this / 255f

    // Converts float brightness (0.0-1.0) to system int (0-255)
    private fun Float.toBrightnessInt(): Int = (this.coerceIn(0f, 1f) * 255).toInt()

    override fun getSystemBrightness(): Flow<Float> {
        // Observe changes from SystemSettingsManager and map Int (0-255) to Float (0.0-1.0)
        return systemSettingsManager.observeSystemBrightness().map { it.toBrightnessFloat() }
    }

    override suspend fun setSystemBrightness(brightness: Float): Result<Unit> {
        val success = systemSettingsManager.setSystemBrightness(brightness.toBrightnessInt())
        return if (success) Result.success(Unit) else Result.failure(Exception("Failed to set system brightness (check permissions)"))
    }

    override fun getOverlayBrightness(): Flow<Float> {
        // Observe the brightness from the shared overlay status
        return overlayService.getOverlayStatus().map { it.brightness }
    }

    override suspend fun setOverlayBrightness(brightness: Float): Result<Unit> {
        // Update the overlay via the service, setting only brightness
        return overlayService.updateOverlay(brightness = brightness, colorTemperatureKelvin = null)
    }

    override fun getAutoBrightnessEnabled(): Flow<Boolean> {
        // Observe changes directly from SystemSettingsManager
        return systemSettingsManager.observeAutoBrightnessEnabled()
    }

    override suspend fun setAutoBrightnessEnabled(enabled: Boolean): Result<Unit> {
        val success = systemSettingsManager.setAutoBrightnessEnabled(enabled)
        return if (success) Result.success(Unit) else Result.failure(Exception("Failed to set auto-brightness mode (check permissions)"))
    }
}