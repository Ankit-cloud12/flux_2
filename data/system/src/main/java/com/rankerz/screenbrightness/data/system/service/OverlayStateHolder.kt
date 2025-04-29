package com.rankerz.screenbrightness.data.system.service

import com.rankerz.screenbrightness.core.domain.model.OverlayStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Singleton holder for the observable state of the screen overlay.
 * This allows the service and its implementation (and potentially other parts
 * of the app) to observe the actual status.
 */
@Singleton
class OverlayStateHolder @Inject constructor() { // Injectable singleton

    private val _overlayStatus = MutableStateFlow(
        OverlayStatus(
            isActive = false, // Initially inactive
            brightness = 1.0f, // Default to full brightness
            colorTemperatureKelvin = 6500 // Default neutral temperature
        )
    )
    val overlayStatus = _overlayStatus.asStateFlow() // Expose as immutable StateFlow

    // Function for the service to update the state
    fun updateStatus(newStatus: OverlayStatus) {
        _overlayStatus.value = newStatus
    }

    // Convenience function for the service to update specific fields
    fun updateStatus(
        isActive: Boolean? = null,
        brightness: Float? = null,
        colorTemperatureKelvin: Int? = null
    ) {
        _overlayStatus.value = _overlayStatus.value.copy(
            isActive = isActive ?: _overlayStatus.value.isActive,
            brightness = brightness ?: _overlayStatus.value.brightness,
            colorTemperatureKelvin = colorTemperatureKelvin ?: _overlayStatus.value.colorTemperatureKelvin
        )
    }
}