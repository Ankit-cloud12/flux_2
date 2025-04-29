package com.rankerz.screenbrightness.data.system.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color // For overlay color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import com.rankerz.screenbrightness.core.domain.model.OverlayStatus
import com.rankerz.screenbrightness.core.common.util.Constants // Import shared constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
// import com.rankerz.screenbrightness.R // Assuming R class is available via app module dependency later
// import com.rankerz.screenbrightness.ui.MainActivity // Assuming MainActivity is accessible

// Removed local constants, using Constants from core:common now

@AndroidEntryPoint
class ScreenOverlayService : Service() {

    @Inject lateinit var stateHolder: OverlayStateHolder // Inject state holder

    private lateinit var windowManager: WindowManager
    private var overlayView: View? = null
    private var currentBrightness: Float = 0.5f // Default
    private var currentTemperatureKelvin: Int = 6500 // Default

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        startForeground(Constants.OVERLAY_NOTIFICATION_ID, createNotification()) // Use shared constant
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Constants.ACTION_SHOW_OVERLAY -> { // Use shared constant
                currentBrightness = intent.getFloatExtra(Constants.EXTRA_BRIGHTNESS, currentBrightness) // Use shared constant
                currentTemperatureKelvin = intent.getIntExtra(Constants.EXTRA_TEMPERATURE, currentTemperatureKelvin) // Use shared constant
                showOverlay()
            }
            Constants.ACTION_HIDE_OVERLAY -> { // Use shared constant
                hideOverlay()
                stopSelf() // Stop the service when hiding
            }
            Constants.ACTION_UPDATE_OVERLAY -> { // Use shared constant
                currentBrightness = intent.getFloatExtra(Constants.EXTRA_BRIGHTNESS, currentBrightness) // Use shared constant
                currentTemperatureKelvin = intent.getIntExtra(Constants.EXTRA_TEMPERATURE, currentTemperatureKelvin) // Use shared constant
                updateOverlay()
            }
        }
        // Use START_STICKY if you want the service to restart if killed (might need adjustments)
        return START_NOT_STICKY
    }

    private fun showOverlay() {
        if (overlayView == null) {
            // Inflate a layout or create a view programmatically
            // For simplicity, creating a basic view here
            overlayView = View(this).apply {
                 // Set initial background based on brightness/temp
                 setBackgroundColor(calculateOverlayColor(currentBrightness, currentTemperatureKelvin))
            }


            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                } else {
                    @Suppress("DEPRECATION")
                    WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY // Deprecated but needed for older APIs
                },
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or // Makes overlay non-interactive
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, // Covers entire screen including status bar
                PixelFormat.TRANSLUCENT // Allows transparency
            ).apply {
                gravity = Gravity.TOP or Gravity.START // Cover entire screen
                // x = 0 // Not needed with MATCH_PARENT
                // y = 0 // Not needed with MATCH_PARENT
            }

            try {
                windowManager.addView(overlayView, params)
                // Update shared state
                stateHolder.updateStatus(isActive = true, brightness = currentBrightness, colorTemperatureKelvin = currentTemperatureKelvin)
            } catch (e: Exception) {
                // Handle exceptions, e.g., permission denied
                e.printStackTrace()
                stopSelf() // Stop if we can't add the view
            }
        } else {
            // If view already exists, just update it
            updateOverlay()
        }
    }

     private fun updateOverlay() {
        overlayView?.let { view ->
            view.setBackgroundColor(calculateOverlayColor(currentBrightness, currentTemperatureKelvin))
            // If layout params need changing (e.g., size), update them here
            // windowManager.updateViewLayout(view, params) // If params changed
        }
         // Update shared state
        stateHolder.updateStatus(isActive = true, brightness = currentBrightness, colorTemperatureKelvin = currentTemperatureKelvin)
    }


    private fun hideOverlay() {
        overlayView?.let {
            try {
                windowManager.removeView(it)
            } catch (e: Exception) {
                e.printStackTrace() // Handle error removing view
            }
            overlayView = null
        }
        // Update shared state
        stateHolder.updateStatus(isActive = false)
    }

    /**
     * Calculates the ARGB color for the overlay based on brightness and temperature.
     * Lower brightness increases alpha (opacity).
     * Lower temperature shifts color towards orange/yellow.
     */
    private fun calculateOverlayColor(brightness: Float, temperatureKelvin: Int): Int {
        // 1. Calculate Alpha (Opacity) based on brightness
        // Inverse relationship: lower brightness = higher alpha
        // Max alpha capped below 255 to prevent pure black screen
        val maxAlpha = 220 // Adjust as needed (e.g., 200-240)
        val alpha = ((1.0f - brightness.coerceIn(0f, 1f)) * maxAlpha).toInt().coerceIn(0, maxAlpha)

        // 2. Calculate Base Tint Color based on temperature
        // Simple interpolation towards an orange/yellow color for warmth
        val warmthFactor = (
            (6500 - temperatureKelvin.coerceIn(1000, 6500)) / 5500.0 // Normalize 1000K-6500K to 1.0-0.0
            ).toFloat().coerceIn(0f, 1f) // 0.0 = neutral (6500K), 1.0 = max warm (1000K)

        // Target warm color (e.g., a shade of orange)
        val targetRed = 255
        val targetGreen = 180 // Adjust for desired warmth level
        val targetBlue = 80   // Significantly reduce blue for warm filter

        // Interpolate RGB components towards the target warm color
        // We interpolate from a base color (let's use black for dimming effect)
        val baseRed = 0
        val baseGreen = 0
        val baseBlue = 0

        val red = (baseRed * (1f - warmthFactor) + targetRed * warmthFactor).toInt().coerceIn(0, 255)
        val green = (baseGreen * (1f - warmthFactor) + targetGreen * warmthFactor).toInt().coerceIn(0, 255)
        val blue = (baseBlue * (1f - warmthFactor) + targetBlue * warmthFactor).toInt().coerceIn(0, 255)

        // 3. Combine Alpha and Tint Color
        return Color.argb(alpha, red, green, blue)
    }


    private fun createNotification(): Notification {
        createNotificationChannel()

        // Intent to open MainActivity when notification is tapped
        // val mainActivityIntent = Intent(this, MainActivity::class.java) // Needs MainActivity reference
        // val pendingIntent = PendingIntent.getActivity(this, 0, mainActivityIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        // Intent for the "Stop" action
        val stopServiceIntent = Intent(this, ScreenOverlayService::class.java).apply {
            action = Constants.ACTION_HIDE_OVERLAY // Use shared constant
        }
        val stopServicePendingIntent: PendingIntent =
            PendingIntent.getService(this, 0, stopServiceIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT) // Use FLAG_UPDATE_CURRENT if intent needs updating

        return NotificationCompat.Builder(this, Constants.OVERLAY_NOTIFICATION_CHANNEL_ID) // Use shared constant
            .setContentTitle("RankerZ Overlay Active")
            .setContentText("Screen filter is running.")
            // Using a system icon as placeholder
            .setSmallIcon(android.R.drawable.ic_dialog_info) // TODO: Replace with actual app icon
            // .setContentIntent(pendingIntent) // Open app on tap
            .setOngoing(true) // Makes the notification non-dismissible
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop", stopServicePendingIntent) // Add Stop action
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.OVERLAY_NOTIFICATION_CHANNEL_ID, // Use shared constant
                "RankerZ Overlay Service",
                NotificationManager.IMPORTANCE_LOW // Low importance for foreground service notification
            ).apply {
                description = "Notification channel for the RankerZ screen overlay service."
                // Configure channel settings if needed (e.g., disable sound)
                setSound(null, null)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        hideOverlay() // Ensure overlay is removed when service stops
    }

    override fun onBind(intent: Intent?): IBinder? {
        // Not used for started services
        return null
    }
}