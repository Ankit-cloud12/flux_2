package com.rankerz.screenbrightness.data.system.manager

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log // For logging errors
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch // Import launch
import javax.inject.Inject
import javax.inject.Singleton

interface SystemSettingsManager {
    /** Gets the current system brightness value (0-255). */
    fun getSystemBrightness(): Int

    /** Sets the system brightness value (0-255). Requires WRITE_SETTINGS permission. */
    fun setSystemBrightness(value: Int): Boolean

    /** Checks if auto-brightness is enabled. */
    fun getAutoBrightnessEnabled(): Boolean

    /** Enables or disables auto-brightness. Requires WRITE_SETTINGS permission. */
    fun setAutoBrightnessEnabled(enabled: Boolean): Boolean

    /** Observes changes to the system brightness value (0-255). */
    fun observeSystemBrightness(): Flow<Int>

    /** Observes changes to the auto-brightness mode setting. */
    fun observeAutoBrightnessEnabled(): Flow<Boolean>
}

@Singleton // Make it a singleton
class SystemSettingsManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SystemSettingsManager {

    private val contentResolver = context.contentResolver

    override fun getSystemBrightness(): Int {
        return try {
            Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS)
        } catch (e: Settings.SettingNotFoundException) {
            Log.e("SystemSettingsManager", "Could not get system brightness", e)
            // Return a default or estimate if needed
            128 // Default to medium brightness
        }
    }

    override fun setSystemBrightness(value: Int): Boolean {
        // Ensure value is within valid range (0-255)
        val brightnessValue = value.coerceIn(0, 255)
        return try {
            // First, disable auto-brightness if setting manually
            setAutoBrightnessEnabled(false)
            // Now set the brightness
            Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, brightnessValue)
        } catch (e: SecurityException) {
            Log.e("SystemSettingsManager", "WRITE_SETTINGS permission denied for setSystemBrightness", e)
            false
        } catch (e: Exception) {
            Log.e("SystemSettingsManager", "Error setting system brightness", e)
            false
        }
    }

     override fun getAutoBrightnessEnabled(): Boolean {
         return try {
             Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
         } catch (e: Settings.SettingNotFoundException) {
             Log.e("SystemSettingsManager", "Could not get auto-brightness mode", e)
             false // Default to false if setting not found
         }
     }

     override fun setAutoBrightnessEnabled(enabled: Boolean): Boolean {
         val mode = if (enabled) Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC else Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
         return try {
             Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, mode)
         } catch (e: SecurityException) {
             Log.e("SystemSettingsManager", "WRITE_SETTINGS permission denied for setAutoBrightnessEnabled", e)
             false
         } catch (e: Exception) {
             Log.e("SystemSettingsManager", "Error setting auto-brightness mode", e)
             false
         }
    }

    override fun observeSystemBrightness(): Flow<Int> = callbackFlow {
        val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) {
                launch { send(getSystemBrightness()) } // Send updated value
            }
        }
        val uri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS)
        contentResolver.registerContentObserver(uri, false, observer)
        // Send the initial value
        send(getSystemBrightness())
        // Unregister observer when flow is cancelled
        awaitClose { contentResolver.unregisterContentObserver(observer) }
    }

    override fun observeAutoBrightnessEnabled(): Flow<Boolean> = callbackFlow {
         val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) {
                launch { send(getAutoBrightnessEnabled()) } // Send updated value
            }
        }
        val uri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE)
        contentResolver.registerContentObserver(uri, false, observer)
        // Send the initial value
        send(getAutoBrightnessEnabled())
        // Unregister observer when flow is cancelled
        awaitClose { contentResolver.unregisterContentObserver(observer) }
    }
}