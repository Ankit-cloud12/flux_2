package com.rankerz.screenbrightness.core.data.service

import android.content.Context
import android.content.Intent
import android.os.Build
import com.rankerz.screenbrightness.core.domain.model.OverlayStatus
import com.rankerz.screenbrightness.core.domain.service.OverlayService
import com.rankerz.screenbrightness.core.common.util.Constants // Import shared constants
import com.rankerz.screenbrightness.data.system.service.OverlayStateHolder // Import the state holder
import com.rankerz.screenbrightness.data.system.service.ScreenOverlayService // Import the actual service
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
// import kotlinx.coroutines.flow.MutableStateFlow // No longer needed
// import kotlinx.coroutines.flow.asStateFlow // No longer needed
import javax.inject.Inject
import javax.inject.Singleton


// Removed local constants, using Constants from core:common now

// TODO: Implement a way to observe the actual service status (e.g., using a BroadcastReceiver or Bound Service) - Still relevant
@Singleton
class OverlayServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val stateHolder: OverlayStateHolder // Inject the state holder
) : OverlayService {

    // No longer need internal state holder
    // private val _overlayStatus = MutableStateFlow(...)

    override fun getOverlayStatus(): Flow<OverlayStatus> {
        // Return the flow directly from the shared state holder
        return stateHolder.overlayStatus
    }

    override fun showOverlay(brightness: Float, colorTemperatureKelvin: Int): Result<Unit> {
        return try {
            val intent = Intent(context, ScreenOverlayService::class.java).apply {
                action = Constants.ACTION_SHOW_OVERLAY // Use shared constant
                putExtra(Constants.EXTRA_BRIGHTNESS, brightness) // Use shared constant
                putExtra(Constants.EXTRA_TEMPERATURE, colorTemperatureKelvin) // Use shared constant
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
            // No longer update local state here; service updates the shared stateHolder
            // _overlayStatus.value = OverlayStatus(true, brightness, colorTemperatureKelvin)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun hideOverlay(): Result<Unit> {
         return try {
            val intent = Intent(context, ScreenOverlayService::class.java).apply {
                action = Constants.ACTION_HIDE_OVERLAY // Use shared constant
            }
            context.startService(intent) // Send intent to trigger stopSelf in service
            // No longer update local state here
            // _overlayStatus.value = _overlayStatus.value.copy(isActive = false)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun updateOverlay(brightness: Float?, colorTemperatureKelvin: Int?): Result<Unit> {
         return try {
            val intent = Intent(context, ScreenOverlayService::class.java).apply {
                action = Constants.ACTION_UPDATE_OVERLAY // Use shared constant
                // Get current values from stateHolder if new ones aren't provided
                val currentStatus = stateHolder.overlayStatus.value
                putExtra(Constants.EXTRA_BRIGHTNESS, brightness ?: currentStatus.brightness) // Use shared constant
                putExtra(Constants.EXTRA_TEMPERATURE, colorTemperatureKelvin ?: currentStatus.colorTemperatureKelvin) // Use shared constant
            }
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                 context.startForegroundService(intent)
             } else {
                 context.startService(intent)
             }
             // No longer update local state here
             // _overlayStatus.value = _overlayStatus.value.copy(...)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}