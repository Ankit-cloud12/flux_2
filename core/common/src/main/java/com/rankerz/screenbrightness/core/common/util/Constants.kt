package com.rankerz.screenbrightness.core.common.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

/**
 * Shared constants used across different modules.
 */
object Constants {

    // Screen Overlay Service Actions & Extras
    const val ACTION_SHOW_OVERLAY = "com.rankerz.screenbrightness.ACTION_SHOW"
    const val ACTION_HIDE_OVERLAY = "com.rankerz.screenbrightness.ACTION_HIDE"
    const val ACTION_UPDATE_OVERLAY = "com.rankerz.screenbrightness.ACTION_UPDATE"
    const val EXTRA_BRIGHTNESS = "extra_brightness"
    const val EXTRA_TEMPERATURE = "extra_temperature"

    // Notification Constants
    const val OVERLAY_NOTIFICATION_CHANNEL_ID = "RankerZ_Overlay_Channel"
    const val OVERLAY_NOTIFICATION_ID = 101

    // DataStore Keys
    val START_ON_BOOT_ENABLED = booleanPreferencesKey("start_on_boot_enabled")
    val APP_THEME = stringPreferencesKey("app_theme")
    // Note: PER_APP_ENABLED key is currently defined in AppProfileRepositoryImpl, consider moving here too.
}